package sylvain.juge.inventory;

import org.testng.annotations.Test;
import sylvain.juge.inventory.util.InvalidParameterException;

import java.io.File;
import java.nio.file.Paths;

import static org.fest.assertions.api.Assertions.assertThat;

public class IndexEntryTest {

    @Test(expectedExceptions = InvalidParameterException.class)
    public void nullHashNotAllowed(){
        new IndexEntry(null, new File("any").toPath());
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void nullPathNotAllowed(){
        new IndexEntry("", null);
    }

    @Test
    public void equalsHashCodeIsReflexive(){
        // a.equals(a)
        IndexEntry entry = new IndexEntry("hash", Paths.get("dummy"));
        sameEqualsHashCodeToString(entry, entry);
    }

    @Test
    public void equalsHashCodeIsSymmetric(){
        // a.equals(b) <=> b.equals(a)
        // identical strings are the same thanks to compiler,
        // thus we have to force them to be different instances
        IndexEntry entry1 = new IndexEntry("hash"+"", Paths.get("dummy"));
        IndexEntry entry2 = new IndexEntry("hash"+"", Paths.get("dummy"));

        sameEqualsHashCodeToString(entry1, entry2);
    }

    @Test
    public void equalsHashCodeWithSameHashCode(){

        IndexEntry entry1 = new IndexEntry("hash1", Paths.get("dummy"));

        // identical strings are the same thanks to compiler,
        // thus we have to force them to be different instances
        IndexEntry sameHash = new IndexEntry(entry1.getHash()+"",Paths.get("anotherPath"));

        // equals but not same instance
        assertThat(sameHash.getHash()).isEqualTo(entry1.getHash());
        assertThat(sameHash.getHash()).isNotSameAs(entry1.getHash());

        assertThat(sameHash.getPath()).isNotEqualTo(entry1.getPath());

        notSameEqualsHashCodeToString(sameHash, entry1);
    }

    @Test
    public void equalsHashCodeWithSamePath(){
        IndexEntry entry2 = new IndexEntry("hash1", Paths.get("dummy"));

        // identical strings are the same thanks to compiler,
        // thus we have to force them to be different instances
        IndexEntry samePathAsEntry2 = new IndexEntry("notSameHash", Paths.get(entry2.getPath().toString()));

        assertThat(samePathAsEntry2.getHash()).isNotEqualTo(entry2.getHash());

        // equals but not same instance
        assertThat(samePathAsEntry2.getPath()).isEqualTo(entry2.getPath());
        assertThat(samePathAsEntry2.getPath()).isNotSameAs(entry2.getPath());

        notSameEqualsHashCodeToString(samePathAsEntry2, entry2);
    }

    private static void sameEqualsHashCodeToString(Object o1, Object o2){
        assertThat(o1).isEqualTo(o2);
        assertThat(o2).isEqualTo(o1);
        assertThat(o1.hashCode()).isEqualTo(o2.hashCode());
        assertThat(o1.toString()).isEqualTo(o2.toString());
    }

    private static void notSameEqualsHashCodeToString(Object o1, Object o2){
        assertThat(o1).isNotEqualTo(o2);
        assertThat(o2).isNotEqualTo(o1);
        assertThat(o1.hashCode()).isNotEqualTo(o2.hashCode());
        assertThat(o1.toString()).isNotEqualTo(o2.toString());
    }


    private static void sameHashCode(Object o1, Object o2){
        assertThat(o1.hashCode()).isNotEqualTo(o2.hashCode());
    }

    // TODO : generate all combinations of valid/invalid parameters
    // - String : null, empty, specific value
    // - Path : null, non existing file, folder, file, file with specific properties
    //
    // design issues :
    // - generate all parameters combinations : quite easy with each possible value of each param
    // - among them, define those that must pass, and those than must not pass
    // -     using code here seems appropriate, but expressiveness is the key

}
