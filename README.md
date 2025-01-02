# Jospi

---

[![license][license img]][license]
[![java version](https://img.shields.io/badge/java-1.8-blue)](https://www.oracle.com/java/technologies/downloads/#java8)
[![maven version][mavenbadge img]][mavenbadge]
[![release][release img]][release]
[![codacy][codacy img]][codacy]
[![sonar][sonar img]][sonar]
[![codefactor][codefactor img]][codefactor]
[![codecov][codecov img]][codecov]

A simple, in-development async wrapper for the osu! API v2

The latest release version can be found at
[GitHub releases](https://github.com/wyvrtn/jospi/releases/)
or at [Maven repository](https://central.sonatype.com/artifact/io.github.wyvrtn/jospi/versions).

_Note: As of current, only Java version 1.8 is supported_

## Table of Contents

- [Quick Start](#quick-start)
- [Contributing](#contributing)
- [Features](#features)
- [Licensing](#licensing)

## Quick Start

### Maven

Note that this project is still in development, so it will be expected that some of the features may not work as intended.

To import using Maven:

```xml
<dependency>
    <groupId>io.github.wyvrtn</groupId>
    <artifactId>jospi</artifactId>
    <version>1.0.1</version>
</dependency>
```

The methods for accessing the API are provided by the `endpoints` field in the class `OsuApiClient`. When creating an instance, you must first create an instance of `ClientCredentialsGrant` or `AuthorizationCodeGrant`. `ClientCredentialsGrant` requires your client ID and client secret, both found in your [osu! settings](https://osu.ppy.sh/home/account/edit#oauth).

The following is an example on how to authorize your client through Client Credentials Grant and using said client to request information on a beatmapset.

```java
// Create an instance of ClientCredentialsGrant
ClientCredentialsGrant grant = new ClientCredentialsGrant(clientId, clientSecret);

// Create an instance of OsuApiClient
OsuApiClient client = new OsuApiClient(grant);

// Optional check on current authorization status. If not already authorized,
// this method will authorize the client through the given authorization grant.
client.ensureAccessToken();

// Get Sotarks' Harumachi Clover, wrapped in a future. (Async)
Future<BeatmapSetExtended> futureSet = client.endpoints.getBeatmapSet(842412);

// Or, immediately get a BeatmapSetExtended object. (Blocking)
BeatmapSetExtended set = client.endpoints.getBeatmapSet(842412).get();

```

## Contributing

Thanks for your interest in contributing to Jospi! Please see the
[Contribution Guidelines](https://github.com/wyvrtn/jospi/blob/master/CONTRIBUTING.md)
for information on how to contribute to the project. This includes creating issues, submitting pull
requests, and setting up your development environment.

## Features

Currently, I'm ahead of minisbett in feature implementation

```text
Beatmap Packs (100% 2/2)
  [✓] /beatmaps/packs
  [✓] /beatmaps/packs/{tag}

Beatmaps (100% 7/7)
  [✓] /beatmaps
  [✓] /beatmaps/lookup
  [✓] /beatmaps/{beatmap}
  [✓] /beatmaps/{beatmap}/attributes
  [✓] /beatmaps/{beatmap}/scores
  [✓] /beatmaps/{beatmap}/scores/users/{user}
  [✓] /beatmaps/{beatmap}/scores/users/{user}/all

Beatmap Sets (100% 2/2)
  [✓] /beatmapsets/lookup
  [✓] /beatmapsets/{beatmapset}

Changelogs (100% 3/3)
  [✓] /changelog
  [✓] /changelog/{buildOrStream}
  [✓] /changelog/{stream}/{build}

Comments (100% 2/2)
  [✓] /comments
  [✓] /comments/{comment}

Events (100% 1/1)
  [✓] /events

Home (100% 1/1)
  [✓] /search

Matches (100% 2/2)
  [✓] /matches
  [✓] /matches/{match}

Multiplayer (100% 2/2)
  [✓] /rooms/{room}/playlist/{playlist}/scores
  [✓] /rooms

News (100% 2/2)
  [✓] /news
  [✓] /news/{news}

Ranking (100% 3/3)
  [✓] /rankings/kudosu
  [✓] /rankings/{mode}/{type}
  [✓] /spotlights

Users (100% 6/6)
  [✓] /users
  [✓] /users/{user}/{mode?}
  [✓] /users/{user}/kudosu
  [✓] /users/{user}/scores/{type}
  [✓] /users/{user}/beatmapsets/{type}
  [✓] /users/{user}/recent_activity

Wiki (100% 1/1)
  [✓] /wiki/{locale}/{path}

Undocumented (0% 0/?)
  To be added
```

## Licensing

Jospi is licensed under the [GNU GPL v3.0 License](LICENSE).
Jospi uses libraries:

- [Apache Http Client](https://hc.apache.org/httpcomponents-client-5.4.x/index.html)
- [Fasterxml Jackson](https://github.com/FasterXML/jackson)
- [Lombok](https://projectlombok.org/)

[license]: https://www.gnu.org/licenses/gpl-3.0
[license img]: https://img.shields.io/badge/License-GPLv3-blue.svg
[mavenbadge]: https://central.sonatype.com/artifact/io.github.wyvrtn/jospi
[mavenbadge img]: https://img.shields.io/maven-central/v/io.github.wyvrtn/jospi
[release]: https://github.com/wyvrtn/jospi/releases/latest
[release img]: https://img.shields.io/github/v/release/wyvrtn/jospi?color=b67721
[codacy]: https://app.codacy.com/gh/wyvrtn/jospi/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade
[codacy img]: https://app.codacy.com/project/badge/Grade/d82cfab8be7043a688f157342147021f
[sonar]: https://sonarcloud.io/summary/new_code?id=wyvrtn_jospi
[sonar img]: https://sonarcloud.io/api/project_badges/measure?project=wyvrtn_jospi&metric=sqale_index
[codecov]: https://codecov.io/gh/wyvrtn/wyvrtn
[codecov img]: https://codecov.io/gh/wyvrtn/wyvrtn/graph/badge.svg?token=ZSZO6U8PWB
[codefactor]: https://www.codefactor.io/repository/github/wyvrtn/jospi
[codefactor img]: https://www.codefactor.io/repository/github/wyvrtn/jospi/badge
