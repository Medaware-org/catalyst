## Medaware Catalyst™

The Backend for the Medaware Platform as well as the _Tangential_ ™ system

# How to dev this locally

## Script

One way to set up the environment is to use the [Setup Script](https://github.com/Medaware-org/setup-script).
Make sure you have Java _17_ installed before invoking the script.

## Manually

### Dependencies

Before anything, please make sure to compile and publish the dependencies:

- [Anterogradia](https://github.com/Medaware-org/anterogradia)
- [AVIS](https://github.com/Medaware-org/antg-avis)

Note, however, that _Anterogradia_ must be built and published **before** _AVIS_!

### Acquiring the source code

In order to acquire this project's source code, execute the following commands:

```shell
git clone https://github.com/Medaware-org/catalyst.git
cd catalyst/
```

Fetch the submodules:

```shell
git submodule init
git submodule update
```

You should now have a `spec.yaml` file in the `openapi` directory.
**Make sure this is the case before proceeding!**

Finally, open the project in your IDE. Gradle may start downloading dependencies
at this point. If this is not the case, make sure the Gradle project is properly loaded.

### SDKs

This project is built with java _17_. Please make sure you have the
appropriate SDK installed.

#### Intellij IDEA

1. Enter the palette (Double <kbd>Shift</kbd>)
2. Find the _Project Structure_ action
3. Go to **Platform Settings** &#8594; **SDKs**
4. Make sure the required JDK is on the list. If not, click <kbd>+</kbd> &#8594;
   **Download JDK ..** and select the appropriate JDK version from the drop-down
   and click **Download**.
5. Finally, go to the **Project Settings** &#8594; **Project** tab and make sure the
   newly installed JDK is selected (Drop-down **SDK**)

### Local Development Environment

**Catalyst** requires some services to be running locally (The
database, MinIO, etc.)
For this purpose, please clone the staging environment and invoke
the docker compose file. Depending on your internet connection, this may
take a while.

```shell
git clone https://github.com/Medaware-org/staging-env
cd staging-env
docker compose up -d
```

# Config

Enter the MinIO interface at `http://localhost:9000` with following credentials:

| Username | Password |
|----------|----------|
| catalyst | medaware |

Proceed with creating an access key and copying said access key into `catalyst.minio.access-key`
in `src/main/resources/application.yaml`.

Finally, create a new bucket `catalyst-resources`.

### Run the backend

At this point, you should be able to run the backend inside your IDE.
