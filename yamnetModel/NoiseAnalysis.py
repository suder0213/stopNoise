import io
import requests  # requests 라이브러리 추가
import tensorflow as tf
import tensorflow_hub as hub
import numpy as np
import csv
import scipy.signal
from flask import Flask, request, jsonify

app = Flask(__name__)

# 모델 로드 (애플리케이션 시작 시 한 번만 로드)
try:
    model = hub.load('https://tfhub.dev/google/yamnet/1')
    class_map_path = model.class_map_path().numpy()
    class_names = []
    # tf.io.gfile.GFile을 사용한 CSV 읽기
    with tf.io.gfile.GFile(class_map_path, 'r') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            class_names.append(row['display_name'])

    def ensure_sample_rate(original_sample_rate, waveform, desired_sample_rate=16000):
        if original_sample_rate != desired_sample_rate:
            desired_length = int(round(float(len(waveform)) / original_sample_rate * desired_sample_rate))
            waveform = scipy.signal.resample(waveform, desired_length)
        return desired_sample_rate, waveform

except Exception as e:
    print(f"Error loading YAMNet model: {e}")
    class_names = []
    model = None

@app.route('/analyze_audio', methods=['POST'])
def analyze_audio_from_url():
    # JSON 요청에서 'url' 키 추출
    data = request.get_json()
    if not data or 'url' not in data:
        return jsonify({"error": "No 'url' provided in JSON body"}), 400

    audio_url = data['url']
    
    try:
        # URL에서 파일 다운로드
        response = requests.get(audio_url)
        response.raise_for_status()  # HTTP 오류 발생 시 예외 발생
        
        # 다운로드한 내용을 메모리 파일 객체로 변환
        file_content = io.BytesIO(response.content)

        sample_rate, wav_data = scipy.io.wavfile.read(file_content, 'rb')
        sample_rate, wav_data = ensure_sample_rate(sample_rate, wav_data)
        
        # 파형 정규화
        waveform = tf.cast(wav_data, tf.float32) / tf.int16.max
        
        # 모델 추론
        scores, embeddings, spectrogram = model(waveform)
        
        # 가장 높은 점수를 가진 클래스 찾기
        scores_np = scores.numpy()
        infered_class_index = scores_np.mean(axis=0).argmax()
        infered_class = class_names[infered_class_index]

        return jsonify({
            "noiseType": infered_class
        })

    except requests.exceptions.RequestException as e:
        return jsonify({"error": f"Failed to download audio file: {e}"}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)