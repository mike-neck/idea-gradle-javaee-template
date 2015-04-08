/*
 * Copyright 2015Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import groovy.xml.MarkupBuilder
import org.gradle.api.*
import org.gradle.api.tasks.TaskAction

class JpaPersistenceXml extends DefaultTask {

    static final String PROJECT_NAME = 'project-name'

    final String fileName = 'persistence.xml'

    final String xmlFileDir = 'jpa/src/main/resources/META-INF'

    final String xmlns = 'http://xmlns.jcp.org/xml/ns/persistence'

    String _version = '2.1'

    String _unitName = 'javaee'

    String _transactionType = 'JTA'

    final Map<String, String> _providers = [eclipse: 'org.eclipse.persistence.jpa.PersistenceProvider']

    String _providerName = 'eclipse'

    String _jdbcDriver = 'org.h2.Driver'

    String _baseJdbcUrl = 'jdbc:h2:tcp://localhost:9092/~/h2'

    String _jdbcUser

    String _jdbcPassword

    Map _jdbcProperties = [:]

    @TaskAction
    void generate() {
        File xmlFile = file("${xmlFileDir}/${fileName}")
        def projectName = file(PROJECT_NAME).text
        def w = new StringWriter()
        w << '<?xml version="1.0" encoding="UTF-8"?>\n'
        def b = new MarkupBuilder(w)
        b.persistence(xmlns: xmlns, version: _version) {
            'persistence-unit'(name: _unitName, 'transaction-type': _transactionType) {
                provider _providers[_providerName]
                if (_transactionType == 'JTA') {
                    'jta-data-source' "jdbc/${projectName}"
                }
                'properties' {
                    property(name: 'javax.persistence.jdbc.driver', value: _jdbcDriver)
                    property(name: 'javax.persistence.jdbc.url', value: "${_baseJdbcUrl}/${projectName}")
                    if (_jdbcUser) {
                        property (name: 'javax.persistence.jdbc.user', value: _jdbcUser)
                    }
                    if (_jdbcPassword) {
                        property (name: 'javax.persistence.jdbc.password', value: _jdbcPassword)
                    }
                    _jdbcProperties.each {
                        property(name: it.key, value: it.value)
                    }
                }
            }
        }
        xmlFile.write(w.toString())
    }

    private File file(String fileName) {
        getProject().file(fileName)
    }

    JpaPersistenceXml version(String v) {
        this._version  = v
        return this
    }

    JpaPersistenceXml version(Double v) {
        this._version = v as String
        return this
    }

    JpaPersistenceXml unitName(String name) {
        this._unitName = name
        this
    }

    JpaPersistenceXml transactionType(String type) {
        this._transactionType = type
        this
    }

    JpaPersistenceXml providerName(String pName) {
        this._providerName = pName
        this
    }

    JpaPersistenceXml jdbcDriver(String driver) {
        this._jdbcDriver = driver
        this
    }

    JpaPersistenceXml baseJdbcUrl(String url) {
        this._baseJdbcUrl = url
        this
    }

    JpaPersistenceXml jdbcUser(String user) {
        this._jdbcUser = user
        this
    }

    JpaPersistenceXml jdbcPassword(String pass) {
        this._jdbcPassword = pass
        this
    }

    JpaPersistenceXml jdbcProperties (Map<String, String> map) {
        _jdbcProperties << map
        this
    }
}
