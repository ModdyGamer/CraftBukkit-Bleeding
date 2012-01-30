package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class ChunkProviderFlatNether implements IChunkProvider {

    private World a;
    private Random b;
    private final boolean c;
    public WorldGenNether netherFortressGen = new WorldGenNether();

    public ChunkProviderFlatNether(World world, long i, boolean flag) {
        this.a = world;
        this.c = flag;
        this.b = new Random(i);
    }

    private void a(byte[] abyte) {
        int i = abyte.length / 256;

        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < i; ++l) {
                    int i1 = 0;

                    if (l == 0) {
                        i1 = Block.BEDROCK.id;
                    } else if (l == 1) {
                        i1 = Block.NETHER_BRICK.id;
                    } else if (l <= 3) {
                        i1 = Block.NETHERRACK.id;
                    }

                    abyte[j << 11 | k << 7 | l] = (byte) i1;
                }
            }
        }
    }

    public Chunk getChunkAt(int i, int j) {
        return this.getOrCreateChunk(i, j);
    }

    public Chunk getOrCreateChunk(int i, int j) {
        byte[] abyte = new byte[16 * this.a.height * 16];
        Chunk chunk = new Chunk(this.a, abyte, i, j);

        this.a(abyte);
        if (this.c) {
            this.netherFortressGen.a(this, this.a, i, j, abyte);
        }

        chunk.initLighting();
        return chunk;
    }

    public boolean isChunkLoaded(int i, int j) {
        return true;
    }

    public void getChunkAt(IChunkProvider ichunkprovider, int i, int j) {
        this.b.setSeed(this.a.getSeed());
        long k = this.b.nextLong() / 2L * 2L + 1L;
        long l = this.b.nextLong() / 2L * 2L + 1L;

        this.b.setSeed((long) i * k + (long) j * l ^ this.a.getSeed());
        if (this.c) {
            this.netherFortressGen.a(this.a, this.b, i, j);
        }
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
        return true;
    }

    public boolean unloadChunks() {
        return false;
    }

    public boolean canSave() {
        return true;
    }

    public List getMobsFor(EnumCreatureType enumcreaturetype, int i, int j, int k) {
        WorldChunkManager worldchunkmanager = this.a.getWorldChunkManager();

        if (worldchunkmanager == null) {
            return null;
        } else {
            BiomeBase biomebase = worldchunkmanager.getBiome(new ChunkCoordIntPair(i >> 4, k >> 4));

            return biomebase == null ? null : biomebase.getMobs(enumcreaturetype);
        }
    }

    public ChunkPosition findNearestMapFeature(World world, String s, int i, int j, int k) {
        return null;
    }
}
