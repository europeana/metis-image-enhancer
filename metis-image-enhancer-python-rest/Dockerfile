FROM ubuntu:22.04

RUN apt-get update  && apt-get upgrade -y
RUN apt-get install git -y
RUN apt-get install python3-pip -y

RUN pip3 install --upgrade pip
RUN pip3 install filetype
RUN pip3 install Flask
RUN pip3 install waitress
RUN pip3 install tensorflow==2.12.0

RUN git clone https://github.com/idealo/image-super-resolution.git \
     && cd image-super-resolution \
     && git checkout v2.2.0 \
     && sed -i 's/tensorflow==2.0.0/tensorflow==2.12.0/' setup.py \
     && sed -i 's/tensorflow-gpu==2.0.0/tensorflow-gpu==2.12.0/' setup.py \
     && python3 setup.py install

COPY src/main/api.py .
COPY src/main/error_handlers.py .
CMD python3 api.py


