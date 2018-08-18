package de.unifrankfurt.dbis.newStructure;

import de.unifrankfurt.dbis.DBFit.NewResultStorage;
import de.unifrankfurt.dbis.Submission.NewScript;
import de.unifrankfurt.dbis.Submission.NewSubmissionExecutor;
import de.unifrankfurt.dbis.Submission.SubmissionParseException;
import de.unifrankfurt.dbis.TestResources;
import de.unifrankfurt.dbis.config.XConfig;
import fit.exception.FitParseException;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.*;
public class NewSubmissionExecutorTest {

    @Test
    public void connectDBFit() throws IOException, SQLException, FitParseException, SubmissionParseException {


        TestResources.Simple simple = TestResources.Simple.getInstance();
        simple.runReset();
        XConfig XConfig = simple.getConfig();
        NewScript script = simple.getReset();

        Connection con = XConfig.newConnection();
        assumeFalse(con.isClosed());
        script.execute(con.createStatement());
        con.close();
        NewSubmissionExecutor exe = new NewSubmissionExecutor(XConfig,
                simple.getSubmission().generateSolution(XConfig), simple.getReset());
        exe.connectDBFit();
        exe.resetDB();
        NewResultStorage result = exe.runCheck(simple.getSubmission());
        String expected = "NewResultStorage{raw='<table>\n" +
                "\t<tr>\n" +
                "\t\t<td>dbfit.MySqlTest</td>\n" +
                "\t</tr>\n" +
                "</table>\n" +
                "\n" +
                "<table> <tr> <td>Connect</td> <td>localhost</td> <td>test</td> <td>test</td> <td>simple</td> </tr> </table>\n" +
                "\n" +
                "<table>\n" +
                "\t<tr>\n" +
                "\t\t<td>Execute</td>\n" +
                "\t\t<td>INSERT INTO simple.color (name,red,green,blue) VALUES\n" +
                "('magenta', 255, 0, 255),\n" +
                "('cyan', 0, 255, 255),\n" +
                "('yellow', 255, 255, 0);\n" +
                "</td>\n" +
                "\t</tr>\n" +
                "</table>\n" +
                "\n" +
                "<table>\n" +
                "\t<tr>\n" +
                "\t\t<td>Execute</td>\n" +
                "\t\t<td>CREATE TABLE `simple`.`animal` (\n" +
                "`name` VARCHAR(45) NOT NULL,\n" +
                "`genus` VARCHAR(45) NOT NULL,\n" +
                "`species` VARCHAR(45) NOT NULL,\n" +
                "PRIMARY KEY (`name`)\n" +
                ");\n" +
                "</td>\n" +
                "\t</tr>\n" +
                "</table>\n" +
                "\n" +
                "<table>\n" +
                "\t<tr>\n" +
                "\t\t<td>Execute</td>\n" +
                "\t\t<td>ALTER TABLE `simple`.`animal`\n" +
                "ADD COLUMN `family` VARCHAR(45) NOT NULL AFTER `name`;\n" +
                "</td>\n" +
                "\t</tr>\n" +
                "</table>\n" +
                "\n" +
                "<table>\n" +
                "\t<tr>\n" +
                "\t\t<td>Execute</td>\n" +
                "\t\t<td>INSERT INTO `simple`.`animal` (`name`, `family`, `genus`, `species`) VALUES\n" +
                "('Tiger', 'Felidae', 'Panthera', 'P. tigris');\n" +
                "</td>\n" +
                "\t</tr>\n" +
                "</table>\n" +
                "\n" +
                "<table>\n" +
                "\t<tr>\n" +
                "\t\t<td>Execute</td>\n" +
                "\t\t<td>/*Welche Farben beinhalten rot?*/\n" +
                "select c.name from color c where c.red &gt; 0;\n" +
                "</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>name</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>magenta</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>red</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>yellow</td>\n" +
                "\t</tr>\n" +
                "</table>\n" +
                "\n" +
                "<table>\n" +
                "\t<tr>\n" +
                "\t\t<td>Execute</td>\n" +
                "\t\t<td>/*alle tiere*/\n" +
                "select a.name from animal a;\n" +
                "</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>name</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>Tiger</td>\n" +
                "\t</tr>\n" +
                "</table>\n" +
                "\n" +
                "', submission=no_name_found, count=Right: 0 Wrong: 0 Ignored: 0 Exception: 0 , logEntry=''}";
    assertEquals(expected,result.toString().trim());
    }
}