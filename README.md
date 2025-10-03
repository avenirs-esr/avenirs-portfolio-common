# avenirs-portfolio-common

Common module for Avenirs ePortfolio

## Features

This module provides shared components organized by feature:

```
src/main/java/fr/avenirsesr/portfolio/common/
├── configuration   Configuration for trace, etc. 
├── data            Data structures, DTOs, and mappers.
├── error           Exception handling and error management.
├── language        Internationalization and language support.
├── openapi         OpenAPI documentation configuration.
├── security        Spring security config and models.
├── temporal        Date, time, period, etc.
├── testutils       Utils classes for tests.
├── validation      Validation classes (mainly a placeholder at this step).
└── web             Context management and queries.
```

## Usage

For now, integrate this module as a Git submodule in your project. Once the code is stable, it will be available as a
Maven dependency.

### Current approach (Git submodule)

```bash
git submodule add https://github.com/your-org/avenirs-portfolio-common.git libs/avenirs-portfolio-common
```

Then add the module to your main project's `pom.xml`:

```xml

<modules>
    <module>libs/avenirs-portfolio-common</module>
</modules>
```

And add it as a dependency:

```xml

<dependency>
    <groupId>fr.avenirsesr.portfolio</groupId>
    <artifactId>avenirs-portfolio-common</artifactId>
    <version>${project.version}</version>
</dependency>
```

### Future approach (Maven dependency)

```xml

<dependency>
    <groupId>fr.avenirsesr.portfolio</groupId>
    <artifactId>avenirs-portfolio-common</artifactId>
    <version>1.0.0</version>
</dependency>
```