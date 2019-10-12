package edivad.waterresources.worldgen.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenCustomStructures implements IWorldGenerator {

	public static final WorldGenStructure goldSeeker = new WorldGenStructure("goldseeker");

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if(world.provider.getDimension() == 0)//OverWorld
		{
			generateStructure(goldSeeker, world, random, chunkX, chunkZ, 400, Blocks.GRASS, BiomePlains.class);
		}
	}

	private void generateStructure(WorldGenerator generator, World world, Random random, int chunkX, int chunkZ, int chance, Block topBlock, Class<?>... classes)
	{
		ArrayList<Class<?>> classesList = new ArrayList<Class<?>>(Arrays.asList(classes));

		int x = (chunkX * 16) + random.nextInt(15);
		int z = (chunkZ * 16) + random.nextInt(15);
		int y = calculateGenerationHeight(world, x, z, topBlock) - 1;

		BlockPos pos = new BlockPos(x, y, z);

		if(world.getWorldType() != WorldType.FLAT)
		{
			if(classesList.contains(world.provider.getBiomeForCoords(pos).getClass()))
			{
				if(random.nextInt(chance) == 0)
				{
					System.out.println("Generate structure at pos " + pos);
					generator.generate(world, random, pos);
				}
			}
		}
	}

	private static int calculateGenerationHeight(World world, int x, int z, Block topBlock)
	{
		int y = world.getHeight();
		boolean foundGround = false;

		while(!foundGround && y-- >= 0)
		{
			Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			foundGround = (block == topBlock);

		}

		return y;
	}
}
