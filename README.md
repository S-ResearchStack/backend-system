# Healthcare Research Platform

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
[TODO]

```bash
$ ./gradlew :platform:ktlintCheck
```
