# Clojure Rest MySQL application

Clojure App to serve a MySQL database through RESTful APIs.

## Clojure libraries used

- [Ring](https://github.com/ring-clojure/ring) - HTTP Webserver
- [Compojure](https://github.com/weavejester/compojure) - Handle RESTful routing for Ring
- [Compojure](https://github.com/weavejester/compojure) - Handle RESTful routing for Ring
- [Clojure java.jdbc](https://github.com/clojure/java.jdbc) - A low-level Clojure wrapper for JDBC-based access to databases
- [next-jdbc](https://github.com/seancorfield/next-jdbc) - The next generation of clojure.java.jdbc: a new low-level Clojure wrapper for JDBC-based access to databases.
- [Clojure Data](https://github.com/clojure/data.json) - Clojure's JSON library - convert data types - to & from JSON

## Usage

- Download the [data](https://istd50043.github.io/project) (.csv to be loaded into a mysql db) and save under /resources folder
- Add the library dependencies into `project.clj` file - should already be there
- `lein run 8000` to run at port 8000
- Test the REST APIs

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
