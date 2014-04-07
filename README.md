hoc2js
======

A command-line tool to convert
[HOCON](https://github.com/typesafehub/config/blob/master/HOCON.md) to JSON.
HOCON is JSON-like but is a config file format for supporting human usage, developed by
[Typesafe](http://typesafe.com/).

## License
The license is Apache 2.0.

## Requirement
- Java 6 or later

## Installation
Get an executable jar from [release page](https://github.com/okapies/hoc2js/releases).

```
$ curl -OL https://github.com/okapies/hoc2js/releases/download/v1.1/hoc2js-1.1.jar
```

## Usage
Input a HOCON from stdin:

```
$ java -jar hoc2js-1.1.jar < test1.conf
```

*hoc2js* can have arguments to input multiple HOCON files (they will be merged, in order):

```
$ java -jar hoc2js-1.1.jar test1.conf test2.conf
```

The input HOCON may contain [substitutions](https://github.com/typesafehub/config/blob/master/HOCON.md#substitutions):

```
$ java -Dprop.test="hello" -jar hoc2js-1.1.jar
test = ${prop.test}
path = ${PATH}
^D
{"test":"hello","path":"/opt/local/bin:/opt/local/sbin:..."}
```

## Configuration
You can override *hoc2js*'s [application.conf](src/main/resources/application.conf) by specifying
system properties.

```
$ java -Dhoc2js.render.formatted=true -jar hoc2js-1.1.jar
```

You can also use *-Dconfig.resource=..., -Dconfig.file=..., or -Dconfig.url=...*. to configure itself.
