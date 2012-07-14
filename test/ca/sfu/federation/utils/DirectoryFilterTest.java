/**
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package ca.sfu.federation.utils;

import java.io.File;
import java.util.Random;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;

/**
 * Directory filter tests.
 * @author dmarques
 */
public class DirectoryFilterTest extends TestCase {

    private Random random = new Random();
    private File testdir;
    
    public DirectoryFilterTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        File tempdir = FileUtils.getTempDirectory();
        testdir = new File(tempdir,"DirectoryFilterTest");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        FileUtils.deleteDirectory(testdir);
    }

    /**
     * 
     */
    public void DirectoryFilterTest() {
        // create a random number of folders
        int count = random.nextInt(10);
        for (int i=0;i<count;i++) {
            File dir = new File(testdir,"dir"+String.valueOf(i));
            dir.mkdirs();
        }
        
    }

} // end class

