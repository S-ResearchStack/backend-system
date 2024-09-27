# Samsung Health Research  Stack Backend System

The backend system for the Samsung Health Research Stack consists of backend services and a data engine available
through application programming interface (API) endpoints that makes it easy to visualize data and manage users for
clinical/medical studies. The stack also includes:

- A software development kit (SDK) for app development
- A web portal for survey creation, participant management, and data visualization

Refer
to <a href="https://developer.samsung.com/health/stack" target="_blank">https://developer.samsung.com/health/stack</a>
for documentation, or jump directly to:

-
The <a href="https://developer.samsung.com/health/stack/developer-guide/installation/install-backend.html" target="_blank">
backend system installation instructions</a>
-
The <a href="https://developer.samsung.com/health/stack/developer-guide/installation/install-sdk.html" target="_blank">
app SDK installation instructions</a>
-
The <a href="https://developer.samsung.com/health/stack/developer-guide/installation/install-portal.html" target="_blank">
web portal installation instructions</a>

## Prerequisites

```
sudo apt install openjdk-17-jdk

// To get proto files
git submodule update --init --recursive
```

## How to build
```
// Build
./gradlew build

// Run
./gradlew run

// Clean
./gradlew clean
```

## How to check coding style

```
./gradlew ktlintCheck
```

## List of environment variable in application.yml

* `AWS_ACCESS_KEY_ID`: Access key ID for AWS
* `AWS_BUCKET_NAME`: AWS S3's bucket name
* `AWS_PRESIGNED_URL_DURATION`: Duration of AWS S3's presigned URL **Default** `60`
* `AWS_REGION`: AWS region **Default** `ap-northeast-2`
* `AWS_SECRET_ACCESS_KEY`: Secret access key for AWS
* `BACKEND_PORT`: Backend server's port number **Default** `50001`
* `CASBIN_DB_HOST`: Host(Endpoint) of Casbin DB
* `CASBIN_DB_PASSWORD`: Password of `CASBIN_DB_USERNAME`
* `CASBIN_DB_PORT`: Port number of Casbin DB
* `CASBIN_DB_SCHEMA`: Schema name of Casbin DB
* `CASBIN_DB_USERNAME`: User account name of Casbin DB
* `DATABASE_TYPE`: Database type. Support `MONGO` only
* `GOOGLE_JWK_URL`: JWK URL of Google **Default** `https://www.googleapis.com/oauth2/v3/certs`
* `MONGODB_DATABASE`: Database name of mongo DB **Default** `health_research`
* `MONGODB_HOST` : URL of mongo DB **Default**
* `MONGODB_PASSWORD`: Password of `MONGODB_USERNAME`
* `MONGODB_USERNAME`: User account name of mongo DB
* `MONGODB_URI` : URI of mongo DB **Default** `URI of mongo DB Atlas`
* `OIDC_GOOGLE_CLIENT_ID`: Client ID of Google OAuth2 credentials from the API Console [Credentials page](https://console.developers.google.com/apis/credentials)
* `OIDC_GOOGLE_CLIENT_SECRET`: Client secret of Google OAuth2 credentials from the API Console [Credentials page](https://console.developers.google.com/apis/credentials)
* `OIDC_GOOGLE_OAUTH2_URL`: Google OAuth2 api URL **Default** `https://oauth2.googleapis.com`
* `OIDC_GOOGLE_REDIRECT_URI`: An authorized redirect URI for the given `OIDC_GOOGLE_CLIENT_ID` specified in the API Console [Credentials page](https://console.developers.google.com/apis/credentials)
* `REDIS_HOST`: Host(Endpoint) of Redis **Default** `127.0.0.1`
* `REDIS_PASSWORD`: Password of Redis
* `REDIS_PORT`: Port number of Redis **Default** `6379`
* `REDIS_SSL_ENABLED`: Whether Redis uses SSL or not **Default** `true`
* `SAMSUNG_ACCOUNT_JWK_URL`: JWK URL of Samsung Account **Default** `https://api.account.samsung.com/v1/oidc/certs`
* `SUPER_TOKENS_URL`: URL of SuperTokens **Default** `http://localhost:3567`
* `TRINO_CATALOG`: Catalog name of Trino **Default** `mongodb`
* `TRINO_HOST`: Host(Endpoint) of Trino **Default** `127.0.0.1`
* `TRINO_PORT`: Port number of Trino **Default** `8080`
* `TRINO_USERNAME`: User account name of Trino **Default** `admin`
