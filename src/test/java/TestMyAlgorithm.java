import com.riot.logPercentile.MyAlgorithm;
import com.riot.logPercentile.PercentileAlgorithm;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

public class TestMyAlgorithm {

    private PercentileAlgorithm testObj;
    int[] data;

    @Before
    public void init() {
        testObj = new MyAlgorithm();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testComputePercentile1() {
        data = new int[100];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }

        Arrays.stream(data).forEach(v -> testObj.accumulate(v));
        Assert.assertEquals(testObj.getPercentileResult(80), 80);
        Assert.assertEquals(testObj.getPercentileResult(20), 20);
        Assert.assertEquals(testObj.getPercentileResult(90), 90);
        Assert.assertEquals(testObj.getPercentileResult(95), 95);
        Assert.assertEquals(testObj.getPercentileResult(99), 99);
    }

    @Test
    public void testComputePercentile2() {
        data = new int[1000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }

        Arrays.stream(data).forEach(v -> testObj.accumulate(v));
        Assert.assertEquals(testObj.getPercentileResult(75), 750);
        Assert.assertEquals(testObj.getPercentileResult(5), 50);
        Assert.assertEquals(testObj.getPercentileResult(50), 500);
        Assert.assertEquals(testObj.getPercentileResult(95), 950);
        Assert.assertEquals(testObj.getPercentileResult(99), 990);
    }

    @Test
    public void testComputePercentile3() {
        data = new int[10000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }

        Arrays.stream(data).forEach(v -> testObj.accumulate(v));
        Assert.assertEquals(testObj.getPercentileResult(60), 6000);
        Assert.assertEquals(testObj.getPercentileResult(1), 100);
        Assert.assertEquals(testObj.getPercentileResult(40), 4000);
        Assert.assertEquals(testObj.getPercentileResult(15), 1500);
        Assert.assertEquals(testObj.getPercentileResult(99), 9900);
    }

    @Test
    public void testComputePercentile4() {
        data = new int[60000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }

        Arrays.stream(data).forEach(v -> testObj.accumulate(v));
        Assert.assertEquals(testObj.getPercentileResult(10), 6000);
        Assert.assertEquals(testObj.getPercentileResult(90), 54000);
        Assert.assertEquals(testObj.getPercentileResult(99), 59400);
    }
}
