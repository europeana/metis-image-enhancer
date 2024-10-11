FROM ubuntu:24.10

RUN apt-get update  && apt-get upgrade -y
RUN apt-get install git -y
RUN apt install python3 python3-pip python3-venv python3-setuptools python3-pil python3-filetype python3-flask python3-waitress -y
RUN mkdir flask-app && cd flask-app
RUN python3 -m venv flask-venv
RUN . flask-venv/bin/activate

RUN flask-venv/bin/pip3 install --upgrade pip
RUN flask-venv/bin/pip3 install filetype
RUN flask-venv/bin/pip3 install Flask
RUN flask-venv/bin/pip3 install waitress
RUN flask-venv/bin/pip3 install uuid
RUN flask-venv/bin/pip3 install pillow
RUN flask-venv/bin/pip3 install tensorflow==2.17.0 numpy==1.26.0

RUN git clone https://github.com/idealo/image-super-resolution.git \
     && cd image-super-resolution \
     && git checkout v2.2.0 \
    && sed -i 's/numpy/numpy==1.26.0/' setup.py \
     && sed -i 's/tensorflow==2.0.0/tensorflow==2.17.0/' setup.py \
     && sed -i 's/tensorflow-gpu==2.0.0/tensorflow-gpu==2.17.0/' setup.py \
     && python3 setup.py install

COPY src/main/api.py .
COPY src/main/mie.py .
COPY src/main/error_handlers.py .
EXPOSE 8080
CMD python3 api.py


