# Healthcare Research Platform
The _Samsung Health Stack Backend System_ is a backend system that makes it easy to analyze data and manage users for clinical/medical research.

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
