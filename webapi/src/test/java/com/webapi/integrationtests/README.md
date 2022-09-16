# *Integration Tests*

## *For Local Debugging of Controller Tests:*

### - Comment out the line that auto-runs spring server

```
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
```

in the controller test you want to debug

### - Change port that integration tests run against

```
private int port;
```

to

```
private int port = 8080;
```

in the controller test you want to debug

### - Change the log level of spring server to INFO

```
logging.level.root=ERROR
```

to

```
logging.level.root=INFO
```

### - Run the web application run configuration

### - Run the controller test

### - Check the console output of web-app/spring