package de.unifrankfurt.dbis;


import de.unifrankfurt.dbis.Submission.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestResources {


    private static Path resourcePath(String path){
        try {
            URI p = TestResources.class.getResource(path).toURI();
            return Paths.get(p);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Path getSampleConfigPath() {
        return resourcePath("/test/config/sampleConfig.txt");
    }

    public static String getSampleConfigString() {
        return "#SQLChecker Configuration File (version: 1.0)\n" +
                "\n" +
                "##### MySQL database Settings #####\n" +
                "#Server name or ip-address (default: localhost)\n" +
                "db.host=192.168.1.1\n" +
                "#Server port (default: 3306)\n" +
                "db.port=1234\n" +
                "#username for authentication (default: root)\n" +
                "db.username=username\n" +
                "#password for authentication\n" +
                "db.password=password\n" +
                "#save password (default: false)\n" +
                "db.savepassword=true\n" +
                "#database name\n" +
                "db.databasename=database\n" +
                "#path to mysql executables\n" +
                "db.executable=exe\n" +
                "\n" +
                "#### Survey Settings #####\n" +
                "#path of the reset script that should be used\n" +
                "db.resetscript=reset.sql\n" +
                "\n" +
                "##### DBFitSolution generator settings #####\n" +
                "#The raw solution file, from which the dbfit solution file should be created\n" +
                "test.infile=raw.sql\n" +
                "#The path at which the output solution file should reside (default: /solution.txt)\n" +
                "test.outfile=solution.txt\n" +
                "#The path at which the sample submission file should reside\n" +
                "# This file shows, how a correct submission might look (default: /submissions/sample.sql)\n" +
                "test.samplefile=sample.sql\n" +
                "\n" +
                "##### Submission executor settings #####\n" +
                "#Folder of the submissions which should be checked (default: /submissions/)\n" +
                "chk.submissions=/subs/\n" +
                "#The path of the dbfit solution file, which should be checked (default: /solution.txt)\n" +
                "chk.solution=solution.txt\n" +
                "#Output path of the log- and result-files (default: /out/)\n" +
                "chk.outpath=/result/\n" +
                "#True, if static queries should be allowed in Student submissions\n" +
                "#These queries do not belong to a specific placeholder in the solution file. They get\n" +
                "executed independently (default: false)\n" +
                "chk.allowstatic=true\n";
    }



    public static DBFitSubmissionData getSubmissionWAuthor(){
        Path path = resourcePath("/test/submission/wAuthor.txt");
        List<Task> taskList = new ArrayList<>();
        taskList.add(
                new TaskSQL(
                        Tags.get("tag"),
                        null, "/* this is comment */\n" +
                        "Select * from * ;"));
        taskList.add(
                new TaskSQL(
                        new Tag("tag2"),
                        null, "/* this is comment 2*/\n" +
                        "SELECT USER(), DATABASE() ;"));
        List<Student> students = new ArrayList<>();
        students.add(new Student("foo","bar","test@test.de"));
        students.add(new Student("schlub","ba","schlubba@test.de"));
        Submission sub = new Submission(students, taskList, "submission");
        String submissionString = "/*authors*/\n" +
                "foo;bar;test@test.de\n" +
                "schlub;ba;schlubba@test.de\n" +
                "\n" +
                "/*tag*/\n" +
                "/* this is comment */\n" +
                "Select * from * ;\n" +
                "\n" +
                "/*tag2*/\n" +
                "/* this is comment 2 */\n" +
                "SELECT USER(), DATABASE() ;";
        return new DBFitSubmissionData(path, sub, submissionString);
    }

    public static class Simple {
        private static Simple instance = new Simple();
        private Path configPath;
        private Submission submission;
        private Path submissionPath;
        private Solution solution;
        private SQLScript reset;
        private Path resetPath;
        private Path dbFitHtmlPath;
        private String dbFitHtml;

        private Simple() {
            this.configPath = null;
            this.submission = null;
            this.submissionPath = resourcePath("/test/simple/raw_simple.txt");
            this.solution = null;
            this.reset = null;
            this.resetPath = resourcePath("/test/simple/reset_simple.txt");
            this.dbFitHtmlPath = resourcePath("/test/simple/dbFitHtml.txt");
            this.dbFitHtml = null;
        }

        public static Simple getInstance() {
            return instance;
        }


        public Submission getSubmission() throws IOException, SubmissionParseException {
            if (submission == null) {
                submission = Submission.fromPath(submissionPath);
            }
            return submission;
        }

        public Path getSubmisionPath() {
            return resourcePath("/test/simple/raw_simple.txt");
        }

        public Solution getSolution() throws IOException, SubmissionParseException {
            if (solution == null) {
                Path dbFitHtml = resourcePath("/test/simple/dbFitHtml.txt");
                Stream<String> lines = Files.lines(dbFitHtml, StandardCharsets.UTF_8);

                solution = new Solution(
                        getSubmission(),
                        lines.collect(Collectors.joining("/n")), null);
            }
            return solution;
        }

        public SQLScript getReset() throws IOException {
            if (reset == null)
                reset = SQLScript.fromPath(getResetPath());
            return reset;
        }

        public Path getResetPath() {
            return resetPath;
        }

        public Path getDbFitHtmlPath() {
            return dbFitHtmlPath;
        }

        public String getDbFitHtml() throws IOException {
            if (dbFitHtml == null)
                dbFitHtml = Files.lines(dbFitHtmlPath)
                        .collect(Collectors.joining("\n"));
            return dbFitHtml;
        }

    }

    public static class DBFitSubmissionData {
        private final Path path;
        private final Submission submission;
        private final String submissionString;

        public DBFitSubmissionData(Path path,
                                   Submission submission,
                                   String submissionString) {
            this.path = path;
            this.submission = submission;
            this.submissionString = submissionString;
        }

        public Path getPath() {
            return path;
        }

        public Submission getSubmission() {
            return submission;
        }

        public String getSubmissionString() {
            return submissionString;
        }

        public Solution getSolution() {
            return null;
        }
    }

}