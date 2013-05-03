package sylvain.juge.inventory;

import org.testng.annotations.Test;
import sylvain.juge.inventory.util.ReadOnlyList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class ItemTest {

    @Test
    public void trees_without_content_but_different_name_must_have_same_hash(){
        Item tree1 = Item.tree("tree1", new ArrayList<Item>());
        Item tree2 = Item.tree("tree2", new ArrayList<Item>());

        assertThat(tree1.getHash()).isEqualTo(tree2.getHash());
    }

    @Test
    public void tree_children_order_by_name_and_does_not_change_tree_hash(){
        Item child1 = Item.file("name1", "hash1");
        Item child2 = Item.file("name2", "hash2");

        Item tree1 = Item.tree("tree1", Arrays.asList(child1, child2));
        assertThat(tree1.getChildren()).containsSequence(child1, child2);

        Item tree2 = Item.tree("tree2", Arrays.asList(child2, child1));
        assertThat(tree1.getChildren()).containsSequence(child1, child2);

        assertThat(tree1.getHash()).isEqualTo(tree2.getHash());
    }

    @Test
    public void tree_children_is_read_only(){

        Item child1 = Item.file("name","hash");
        List<Item> items = new ArrayList<>();
        items.add(child1);

        Item tree = Item.tree("tree1", items);

        assertThat(tree.getChildren()).isInstanceOf(ReadOnlyList.class);

    }

    // order of items should not matter when computing tree hash
}
