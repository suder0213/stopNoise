# 1. TensorFlow가 미리 설치된 공식 이미지를 기반으로 사용합니다.
#    이 이미지는 GPU가 없는 환경에서 자동으로 CPU를 사용하도록 설정되어 있습니다.
FROM tensorflow/tensorflow:2.16.1

# 2. GPU 사용을 명시적으로 비활성화하는 환경 변수를 설정합니다.
#    이렇게 하면 GPU를 찾는 불필요한 과정이 생략됩니다.
ENV CUDA_VISIBLE_DEVICES=""

# 3. 컨테이너 내부의 작업 디렉터리를 /app으로 설정합니다.
WORKDIR /app

# 4. pip 설치에 필요한 의존성 파일(requirements.txt)을 복사합니다.
#    아직 파일이 없다면 다음 단계에서 만들게 될 것입니다.
COPY requirements.txt .

# 5. 필요한 모든 파이썬 라이브러리를 설치합니다.
RUN pip install --no-cache-dir --ignore-installed -r requirements.txt

# 6. 모든 파이썬 소스 코드(.py 파일)를 컨테이너에 복사합니다.
COPY . .

# 7. 컨테이너가 5000번 포트를 외부에 노출하도록 설정합니다.
EXPOSE 5000

# 8. 컨테이너가 시작될 때 실행할 명령어입니다.
CMD ["python", "NoiseAnalysis.py"]