/*
 * Copyright 2015Shinya Mochida
 * <p>
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.gradle.api.*
import org.gradle.api.tasks.TaskAction
import java.nio.file.*

import static java.nio.file.FileVisitResult.*

class RemovingDummyTask extends DefaultTask {

    static final String DUMMY_FILE = 'dummy'

    static final FileVisitor<Path> VISITOR = [
            postVisitDirectory: {Path dir, IOException e ->
                if(e != null) throw e
                CONTINUE
            }, preVisitDirectory: {Path dir, Object attr ->
                println "visit directory : ${dir}"
                CONTINUE
            }, visitFile: {Path file, Object attr ->
                if (file.endsWith(DUMMY_FILE)) {
                    println "remove file : ${file}"
                    Files.deleteIfExists(file)
                }
                CONTINUE
            }, visitFileFailed: {Path file, IOException e ->
                if(e != null) throw e
                CONTINUE
            }] as FileVisitor

    @TaskAction
    void removeDummyFiles() {
        def dirs = ['app', 'jpa', 'web']
        dirs.each {dir ->
            File d = getProject().file(dir)
            def path = d.toPath()
            Files.walkFileTree(path, VISITOR)
        }
    }
}
