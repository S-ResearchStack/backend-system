# Samsung Health Stack Backend System
The backend system for the Samsung Health Stack consists of backend services and a data engine available through application programming interface (API) endpoints that makes it easy to analyze data and manage users for clinical/medical studies. The stack also includes:

-   A software development kit (SDK) for app development
-   A web portal for survey creation, participant management, and data analysis

Refer to https://s-healthstack.io for documentation, including complete installation instructions and getting started tutorial.

## Getting Started

### Prerequisite
- java 17

### Build
```bash
# clean all modules
$ ./gradlew clean

# build all modules
$ ./gradlew build

# build only platform module
$ ./gradlew :platform:build -x test
```

### Unit Test
```bash
# test all modules
$ ./gradlew test

# test only platform
$ ./gradlew :platform:test
```

### Check coding Style
```bash
$ ./gradlew ktlintCheck
```
