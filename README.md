### Medaware Catalyst&trade;

### Building

This project uses the following Medaware dependencies:

```kotlin
implementation("org.medaware:anterogradia:+")
implementation("org.medaware:antg-mdk:+")
```

Thus, both **Anterogradia** and the **ANTG MDK Library** need to be built and compiled beforehand, preferably in that order, since
the latter depends on the former.