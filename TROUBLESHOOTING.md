# TroubleShooting:
- You might see an error "java.lang.IllegalArgumentException: Password cannot be set empty"
- this means you are missing the environment variable: JASYPT_ENCRYPTOR_PASSWORD=secretkey
- If you are having issues with github actions, it is valuable to look at the maven
  plugins tab, and run things there to see what is failing, that is closer to what github actions will use
- Pretty mush anything that is complaining about a missing password needs this env var:
  JASYPT_ENCRYPTOR_PASSWORD=secretkey
  - Maven surefire plugin, run configuration, the server itself, tests, etc

## TroubleShooting Dependencies
- Googling the name of the artifact ID and looking at version information in mvn repo
  can help you identify versioning mismatching and versions to try
- https://start.spring.io/ is an invaluable tool for both learning what is out there,
  and validating version compatibility
