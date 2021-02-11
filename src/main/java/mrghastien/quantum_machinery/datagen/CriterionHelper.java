package mrghastien.quantum_machinery.datagen;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;

public class CriterionHelper {

	public static InventoryChangeTrigger.Instance hasItem(net.minecraft.util.IItemProvider itemIn) {
        return hasItem(ItemPredicate.Builder.create().item(itemIn).build());
    }

	public static InventoryChangeTrigger.Instance hasItem(Tag<Item> tagIn) {
        return hasItem(ItemPredicate.Builder.create().tag(tagIn).build());
    }

    public static InventoryChangeTrigger.Instance hasItem(ItemPredicate... predicates) {
        return InventoryChangeTrigger.Instance.forItems(predicates);
    }
}