# 📦 Changelog

This file tracks all notable changes to this repository, following
the [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) format
and [Conventional Commits](https://www.conventionalcommits.org/) standard.

---

## [v1.4.0]

- Combined security filters, hmac or Api key, to be usable by the frontend or another microservice.

## [v1.3.1]

- Seeder for avenirs portfolio API moved in the associated repository.
- Missing property for the seeder added in the test property file.
- husky pre push patched adapted to check on ly not already pushed commit message (the merge commit can be not
  compliant)

## [v1.2.0] - 2025-10-01

- Shared objects for back office.

## [v1.1.0] - 2025-09-23

- Feature security fetched from avenirs-portfolio-ap.

## [v1.0.0] - 2025-09-12

- ✨ **Repository initialization**
    - Commitlint and husky,
    - Changelog,
    - sdkman,
    - nvm,
    - linter,
    - tests.

## Features

Note: for this first version, the files have been moved from the shared feature of avenirs-portfolio-api and reorganized
into features.

```
src/main/java/fr/avenirsesr/portfolio/common/
├── data        Data structures, DTOs, and mappers.
├── error       Exception handling and error management.
├── language    Internationalization and language support.
├── openapi     OpenAPI documentation configuration.
├── temporal    Date, time, period, etc.
├── testutils   Utils classes for tests.
├── validation  Validation classes (mainly a placeholder at this step).
└── web         Context management and queries.
```
