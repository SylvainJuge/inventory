package sylvain.juge.inventory.util;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static sylvain.juge.inventory.AssertThrows.assertThrows;

public class ReadOnlyListTest {

    @Test
    public void copy_on_create() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        ReadOnlyList<Integer> roList = new ReadOnlyList<>(list);
        list.clear();
        assertThat(list).isEmpty();
        assertThat(roList).containsExactly(1, 2, 3);
    }

    @Test
    public void clear_and_remove_operations_are_not_supported() {
        final ReadOnlyList<Integer> roList = new ReadOnlyList<>(1, 2, 3);
        assertThat(roList).containsExactly(1,2,3);
        assertThrows(UnsupportedOperationException.class, new Runnable() {
            @Override
            public void run() {
                roList.clear();
            }
        });
        assertThrows(UnsupportedOperationException.class, new Runnable() {
            @Override
            public void run() {
                roList.removeAll(Arrays.asList(1));
            }
        });
        assertThrows(UnsupportedOperationException.class, new Runnable() {
            @Override
            public void run() {
                roList.remove(0);
            }
        });

    }

}
