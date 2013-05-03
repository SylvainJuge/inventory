package sylvain.juge.inventory;

import static org.fest.assertions.api.Assertions.assertThat;

public final class AssertThrows {

    private AssertThrows(){
        // uncallable constructor
    }

    public static <T extends Throwable> void assertThrows(Class<T> expected, Runnable task){
        Throwable thrown = null;
        try {
            task.run();
        } catch (Throwable t){
            thrown = t;
        }
        assertThat(thrown).isInstanceOf(expected);
    }
}
