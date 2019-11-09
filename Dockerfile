FROM openjdk:11-jre-slim

# Install ExifTool
# Adapted from: https://github.com/Miljar/exiftool-docker/blob/master/Dockerfile
# ExifTool versions: 10.20, 11.50, 11.75
ENV EXIFTOOL_VERSION=11.50
RUN apt-get -qq update && apt-get -qq install -y --no-install-recommends perl make wget moreutils && apt-get -qq clean
RUN cd /tmp \
	&& wget -q http://www.sno.phy.queensu.ca/~phil/exiftool/Image-ExifTool-${EXIFTOOL_VERSION}.tar.gz \
	&& tar -zxf Image-ExifTool-${EXIFTOOL_VERSION}.tar.gz \
	&& cd Image-ExifTool-${EXIFTOOL_VERSION} \
	&& perl Makefile.PL \
	&& chronic make test \
	&& chronic make install \
	&& cd .. \
	&& rm -rf Image-ExifTool-${EXIFTOOL_VERSION}


RUN mkdir /app

WORKDIR /app

ADD target/media-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "media-1.0-SNAPSHOT.jar"]
