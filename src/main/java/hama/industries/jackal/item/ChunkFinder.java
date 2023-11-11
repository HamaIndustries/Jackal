package hama.industries.jackal.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ChunkFinder extends Item {

    public static final String ID = "chunk_finder";

    public ChunkFinder(Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        System.out.println(count);
        super.onUsingTick(stack, player, count);
    }
}
