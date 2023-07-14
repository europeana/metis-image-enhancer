[![CI](https://github.com/europeana/metis-image-enhancer/actions/workflows/ci.yml/badge.svg)](https://github.com/europeana/metis-image-enhancer/actions/workflows/ci.yml) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=europeana_metis-image-enhancer&metric=coverage)](https://sonarcloud.io/summary/new_code?id=europeana_metis-image-enhancer)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=europeana_metis-image-enhancer&metric=bugs)](https://sonarcloud.io/summary/new_code?id=europeana_metis-image-enhancer) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=europeana_metis-image-enhancer&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=europeana_metis-image-enhancer)  
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=europeana_metis-image-enhancer&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=europeana_metis-image-enhancer) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=europeana_metis-image-enhancer&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=europeana_metis-image-enhancer)  
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=europeana_metis-image-enhancer&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=europeana_metis-image-enhancer) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=europeana_metis-image-enhancer&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=europeana_metis-image-enhancer)  
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=europeana_metis-image-enhancer&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=europeana_metis-image-enhancer)

# Metis image enhancer repository
## Table of Contents
- [Table of Contents](#table-of-contents)
- [About the Project](#about-the-project)
- [Description](#description)
- [Built With](#built-with)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites-and-dependencies)
- [Installation](#installation-of-library)
- [Usage](#usage)
- [Example](#example)
- [Architecture](#design-architecture)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [Acknowledgements](#acknowledgements)

# About the Project

# Description
Library that calls python api from java. The library uses image enhancement (super resolution)
on small thumbnails, and performs a simple sharpen filter.

# Built With
* [Python](https://www.python.org/)
* [Java](https://www.java.com)
* [Test containers](https://testcontainers.com/)
* [Junit](https://junit.org/junit5/)
* [Archunit](https://www.archunit.org/)

# Getting Started
# Prerequisites and Dependencies

* [Docker Engine v24.0.4](https://docs.docker.com/engine/)
* [Docker Compose v2.19.1](https://docs.docker.com/compose/)

# Installation of library
### Get the repository
```shell
git clone https://github.com/europeana/metis-image-enhancer.git
```

### Build docker ISR API
go to the cloned folder of the library /metis-image-enhancer/src/main/resources/isr,
and run the following command to build the image enhancer application
```shell
docker compose build --no-cache
```

# Usage
#### Enable Python API Endpoint for image enhancement
go to the cloned folder of the library /metis-image-enhancer/src/main/resources/isr, 
and run the following commands to get the service up and running.
this will run with 3 nodes
```shell
docker compose up --scale israpp=3
```
the application uses a nginx server to load balance the requests using the least connection algorithm
between the instances. If more computing power is needed the app can upscale to the amount of resources needed.

### 1. Image enhancement is possible with the command:
```shell
curl --request POST http://localhost:5050/enhance/image --form image=@./metis-image-enhancer-lib/src/test/resources/img/thumbnail.jpg \ 
                                                        --output ./metis-image-enhancer-lib/src/test/resources/img/thumbnail_enhanced.jpg
```
Response:
```
HTTP code: 200 
Content-Type: image/jpeg
Response Body: Enhanced image to download
``` 

### 2. Client java library
Note: Java and Maven are prerequisites, the library adds sharpen filter extra to the python api result.

#### installation
```shell
mvn clean install
```

#### usage
add dependency in your project
````xml
<dependency>
    <groupId>eu.europeana.metis.isr</groupId>
    <artifactId>metis-image-enhancer-lib</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
````
### properties
By default, the following properties are set in the library if you want to change the address or port, and the timeout 
are in seconds.
```
api.url = http://localhost:5050
connect.timeout = 300
read.timeout = 300
```

See demo project
```java
package eu.europeana.metis.isr;

import eu.europeana.metis.isr.domain.service.ImageSuperResolutionService;
import eu.europeana.metis.isr.library.ImageSuperResolutionLibrary;
import eu.europeana.metis.isr.library.LibServiceType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * The type Main.
 */
public class Main {
  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) throws IOException {
    ImageSuperResolutionService service = ImageSuperResolutionLibrary.getService(LibServiceType.API);

    BufferedImage outputBufferedImage = service.enhance(
            Paths.get("demo", "src", "main", "resources", "img", "europeana_demo.png")
                    .toAbsolutePath()
                    .toString());

    ImageIO.write(outputBufferedImage, "png",
            new File(Paths.get("demo", "src", "main", "resources", "img", "europeana_demo_enhanced.png")
                    .toAbsolutePath()
                    .toString()));
  }
}
```
# Example
The following image shows the outcome:
## Original
![demo original](./images/europeana_demo.png)
## Enhanced
![demo enhanced](./images/europeana_demo_enhanced.png)

# Design architecture
![architecture](./images/architecture.png)

The motivation behind the design summarizes in the following concept:
"Communication goes from outer layers to the inner layers."

- The library layer contains interactions details between the configuration and the library.
- The configuration layer provides the properties needed to connect with the adapter.
- The infrastructure layer contains the adapter pattern implementation to plug in any thumbnail adapter 
  to do the image enhancement.
- The domain contains the model and service core business logic, so this remains untouched by external changes
  of the outer layers as much as possible.

# Roadmap
- [x] library initial release.
- [ ] ...

# Contributing
Any contributions you make are appreciated.

1. Fork the Project
2. Create your Feature Branch (git checkout -b feature/AmazingFeature)
3. Commit your Changes (git commit -m 'Add some AmazingFeature')
4. Push to the Branch (git push origin feature/AmazingFeature)
5. Open a Pull Request

# Acknoledgements
This project is powered by
[Image Super Resolution v2.2.0](https://github.com/idealo/image-super-resolution)
