# Payments API

## The problem

- Restful API
- API verbs/methods to be implemented
  - Fetch a payment resource
  - Create, update and delete payment resource
  - List a collection of payment resources
- Persist resource state


## Design



### Document database
Since the api is only about payments and there's no other requirement about relations with another entities I decide to
store the payments in a document database (mongodb) in the form of a [PaymentDocument](./src/main/java/com/example/payments/repository/PaymentDocument.java)

There's also a few DTOs wich can convert to and from a PaymentDocument, the idea es to decouple the input/output format from the
actual database representation

The actual implementation uses an embedded mongo database, just for simplicity, in a production environment we will use
a real mongodb cluster.

**Important: First time the application starts, it might take a while, since a embedded mongo is downloaded**

## Implementation

### Programming language and Frameworks

I had used Spring Boot 2.0.0 framework. It provides an easy way to create a rest service with production ready features (actuator module) and
and mongodb integration.

Along with a Reactive Streams library (Reactor) for using a reactive mongo driver and tomcat 8.5 nio with servlet 3.1 this configuration provides non-blocking async request dispatching in the server and in the user code. 
which should support more load than blocking io.

See reasoning here [link](.http://callistaenterprise.se/blogg/teknik/2014/04/22/c10k-developing-non-blocking-rest-services-with-spring-mvc/)

I also added spring security to add cors and some security headers
- Content Security Policy (CSP)
- X-Frame-Options
- Content-Type-Options
- Referrer policy
- X-XSS-Protection

CSFR is disable since we do not have authentication

### Test

Since the exercise states

`You should use best practice, for example TDD/BDD, with a focus on full-stack testing`

I had used cucumber for a BDD approach.

The test are running against the actual api and can be found [here](./src/test/resources/payments.feature)

There's only one unit test, since the integration test covers the functionality and almost all components are spring
magic based


### How to run tests locally

```bash
./gradlew test
```

### How to run the service locally

```bash
./gradlew bootRun
```

### Or create a distrubution zip package

```bash
./gradlew bootZip
```

### Restful API specification

Default specification can be found [here](./src/main/java/com/example/payments/v0/client/PaymentsClient.java)
or in [here](./src/main/java/com/example/payments/v0/controllers/PaymentsController.java)

```
@FeignClient(value = "payments", decode404 = true)
 public interface PaymentsClient {

     @RequestMapping(method = RequestMethod.GET, value = "/v0/payments/{paymentId}", produces = "application/json;charset=UTF-8")
     public ResponseEntity<PaymentData> findPayment(@PathVariable("paymentId") UUID paymentId);

     @RequestMapping(method = RequestMethod.GET, value = "/v0/payments", produces = "application/json;charset=UTF-8")
     public ResponseEntity<PaymentDataList> findPayments();

     @RequestMapping(method = RequestMethod.POST, value = "/v0/payments", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
     public ResponseEntity<PaymentData> newPayment(@RequestBody PaymentDefinition newPayment);

     @RequestMapping(method = RequestMethod.PUT, value = "/v0/payments/{paymentId}", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
     public ResponseEntity<PaymentData> updatePayment(@PathVariable("paymentId") UUID paymentId, @RequestBody PaymentDefinition newPayment);

     @RequestMapping(method = RequestMethod.DELETE, value = "/v0/payments/{paymentId}", produces = "application/json;charset=UTF-8")
     public ResponseEntity<PaymentData> deletePayment(@PathVariable("paymentId") UUID paymentId);

 }
```

#### **Healthcheck:**
- Endpoint: `GET    /actuator/health`

Example: `{"status":"UP"}`

Based on spring actuator healthcheck it's returning UP and a 200 if the application is ready to serve requests.

It is also checking if we have connection to mongo, it's configured [here](./src/main/resources/application.yml)

This endpoint is basically needed to support Load Balancing and Services Discovery


#### **Metrics List:**
- Endpoint: `GET    /actuator/metrics`
Expose basic metrics in jmx and in http formats.
The endpoint can be configured to integrated with monitoring tools like datadog or prometheus

Example:

```{
   "names": [
     "jvm.buffer.memory.used",
     "jvm.memory.used",
     "jvm.buffer.count",
     "jvm.gc.memory.allocated",
     "logback.events",
     "process.uptime",
     "jvm.memory.committed",
     "system.load.average.1m",
     "jvm.gc.pause",
     "jvm.gc.max.data.size",
     "jvm.buffer.total.capacity",
     "jvm.memory.max",
     "system.cpu.count",
     "system.cpu.usage",
     "process.files.max",
     "jvm.threads.daemon",
     "jvm.threads.live",
     "process.start.time",
     "jvm.classes.loaded",
     "jvm.classes.unloaded",
     "jvm.threads.peak",
     "jvm.gc.live.data.size",
     "jvm.gc.memory.promoted",
     "process.files.open",
     "process.cpu.usage",
     "http.server.requests"
   ]
 }
 ```

#### **Metrics Detail:**
- Endpoint: `GET    /actuator/metrics/{metric}`
Given an metric obtained in the metrics List it returns the infor about the metric

Example:
`GET    /actuator/metrics/jvm.memory.used `
```{
   "name": "jvm.memory.used",
   "measurements": [
     {
       "statistic": "VALUE",
       "value": 134282632
     }
   ],
   "availableTags": [
     {
       "tag": "area",
       "values": [
         "heap",
         "nonheap"
       ]
     },
     {
       "tag": "id",
       "values": [
         "Compressed Class Space",
         "PS Old Gen",
         "PS Survivor Space",
         "Metaspace",
         "PS Eden Space",
         "Code Cache"
       ]
     }
   ]
 }
 ```