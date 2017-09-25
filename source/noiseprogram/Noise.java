package noiseprogram;

import shophorn.Maths;

public class Noise
{
    private static final int hashMask = 255;
    private static final int [] hash = {
        164,38,10,207,148,133,126,172,17,70,20,8,150,229,158,113,
        194,219,212,43,4,72,48,156,243,61,226,47,101,80,249,168,
        110,182,163,200,144,140,189,52,86,254,58,117,204,26,3,152,
        68,5,240,30,180,44,98,115,225,155,248,252,1,230,64,127,
        75,149,154,9,108,118,121,102,42,206,170,11,106,63,214,78,
        59,54,95,235,116,57,153,2,253,53,234,143,178,193,92,77,
        91,56,62,90,73,151,49,174,131,255,221,104,6,23,161,60,
        50,14,250,13,141,167,89,188,175,210,119,15,129,142,199,187,
        197,179,132,205,227,94,185,85,24,173,76,67,83,74,136,196,
        220,224,35,223,176,165,242,146,65,157,138,232,217,87,84,184,
        134,202,112,79,21,236,103,7,244,100,239,135,114,147,228,105,
        31,107,55,251,97,123,125,231,27,201,186,245,191,166,238,145,
        37,218,46,45,215,120,169,69,19,96,25,162,32,33,124,247,
        16,82,171,12,137,160,190,195,28,88,40,192,22,233,29,111,
        211,81,36,213,128,241,109,222,216,39,183,203,41,0,198,122,
        237,18,159,139,71,51,209,66,246,99,181,208,177,130,34,93,

        164,38,10,207,148,133,126,172,17,70,20,8,150,229,158,113,
        194,219,212,43,4,72,48,156,243,61,226,47,101,80,249,168,
        110,182,163,200,144,140,189,52,86,254,58,117,204,26,3,152,
        68,5,240,30,180,44,98,115,225,155,248,252,1,230,64,127,
        75,149,154,9,108,118,121,102,42,206,170,11,106,63,214,78,
        59,54,95,235,116,57,153,2,253,53,234,143,178,193,92,77,
        91,56,62,90,73,151,49,174,131,255,221,104,6,23,161,60,
        50,14,250,13,141,167,89,188,175,210,119,15,129,142,199,187,
        197,179,132,205,227,94,185,85,24,173,76,67,83,74,136,196,
        220,224,35,223,176,165,242,146,65,157,138,232,217,87,84,184,
        134,202,112,79,21,236,103,7,244,100,239,135,114,147,228,105,
        31,107,55,251,97,123,125,231,27,201,186,245,191,166,238,145,
        37,218,46,45,215,120,169,69,19,96,25,162,32,33,124,247,
        16,82,171,12,137,160,190,195,28,88,40,192,22,233,29,111,
        211,81,36,213,128,241,109,222,216,39,183,203,41,0,198,122,
        237,18,159,139,71,51,209,66,246,99,181,208,177,130,34,93
    };

    public static double value1D (double x, double frequency)
    {
        x *= frequency;

        int i0 = (int) x;
        int i1 = i0 + 1;

        double t = Maths.smooth(x - i0);

        int h0 = i0 & hashMask;
        int h1 = i1 & hashMask;

        double v0 = (double)hash[h0] / hashMask;
        double v1 = (double)hash[h1] / hashMask;

        return (1 - t) * v0 + t * v1;
    }

    public static double value2D (double x, double y, double frequency)
    {
        x *= frequency;
        y *= frequency;

        int ix0 = (int)Math.floor(x);
        int iy0 = (int)Math.floor(y);
        int ix1 = ix0 + 1;
        int iy1 = iy0 + 1;

        double tx = Maths.smooth (x - ix0);
        double ty = Maths.smooth (y - iy0);

        int h0 = hash [ix0 & hashMask];
        int h1 = hash [ix1 & hashMask];
        int h00 = hash [h0 + (iy0 & hashMask)];
        int h10 = hash [h1 + (iy0 & hashMask)];
        int h01 = hash [h0 + (iy1 & hashMask)];
        int h11 = hash [h1 + (iy1 & hashMask)];

        return Maths.lerp (
            Maths.lerp (h00, h10, tx),
            Maths.lerp (h01, h11, tx),
            ty
        ) / hashMask;
    }

    public static double value3D (double x, double y, double z, double frequency)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        int ix0 = (int)Math.floor(x);
        int iy0 = (int)Math.floor(y);
        int iz0 = (int)Math.floor(z);
        int ix1 = ix0 + 1;
        int iy1 = iy0 + 1;
        int iz1 = iz0 + 1;

        double tx = Maths.smooth (x - ix0);
        double ty = Maths.smooth (y - iy0);
        double tz = Maths.smooth (z - iz0);

        int h0 = hash [ix0 & hashMask];
        int h1 = hash [ix1 & hashMask];

        int h00 = hash [h0 + (iy0 & hashMask)];
        int h10 = hash [h1 + (iy0 & hashMask)];
        int h01 = hash [h0 + (iy1 & hashMask)];
        int h11 = hash [h1 + (iy1 & hashMask)];

        int h000 = iz0 & hashMask;
        int h001 = iz1 & hashMask;

        double v000 = hash [h00 + h000];
        double v100 = hash [h10 + h000];
        double v010 = hash [h01 + h000];
        double v110 = hash [h11 + h000];
        double v001 = hash [h00 + h001];
        double v101 = hash [h10 + h001];
        double v011 = hash [h01 + h001];
        double v111 = hash [h11 + h001];

        // return Maths.lerp (
        //     Maths.lerp (Maths.lerp (v000, v100, tx), Maths.lerp(v010, v110, tx), ty),
        //     Maths.lerp (Maths.lerp (v001, v101, tx), Maths.lerp(v011, v111, tx), ty),
        //     tz
        // ) / hashMask;

        // return (
        //     (1-tz) * ((1-ty) * ((1-tx) * v000 + tx * v100) + ty * ((1-tx) * v010 + tx * v110)) +
        //     tz * ((1-ty) * ((1-tx) * v001 + tx * v101) + ty * ((1-tx) * v011 + tx * v111))
        // ) / hashMask;        

        // return (
        //     (1-tz) * (1-ty) * (1-tx) * v000 +
        //     (1-tz) * (1-ty) * tx * v100 +
        //     (1-tz) * ty * (1-tx) * v010 +
        //     (1-tz) * ty * tx * v110 +
        //     tz * (1-ty) * (1-tx) * v001 +
        //     tz * (1-ty) * tx * v101 +
        //     tz * ty * (1-tx) * v011 +
        //     tz * ty * tx * v111
        // ) / hashMask;      

            // return Maths.lerp (
            //     (
            //         (v000 + (v100 - v000) * tx) +
            //         (v010 + (v110 - v010) * tx) * ty -
            //         (v000 + (v100 - v000) * tx) * ty
            //     ), 
            //     (
            //         (v001 + (v101 - v001) * tx) +
            //         (v011 + (v111 - v011) * tx) * ty -
            //         (v001 + (v101 - v001) * tx) * ty
            //     ),
            //     tz
            // ) / hashMask;

        return
            (
                (v000 + (v100 - v000) * tx)
                + (v010 + (v110 - v010) * tx) * ty
                - (v000 + (v100 - v000) * tx) * ty
                + (v001 + (v101 - v001) * tx) * tz
                - (v000 + (v100 - v000) * tx) * tz
                + (v011 + (v111 - v011) * tx) * ty * tz
                - (v001 + (v101 - v001) * tx) * ty * tz
                - (v010 + (v110 - v010) * tx) * ty * tz
                + (v000 + (v100 - v000) * tx) * ty * tz
        ) / hashMask;

        // lerp: (1-t) * a + t * b
        // lerp: a + (b - a)t



        // int h000 = hash [h00 + (iz0 & hashMask)];
        // int h100 = hash [h10 + (iz0 & hashMask)];
        // int h010 = hash [h01 + (iz0 & hashMask)];
        // int h110 = hash [h11 + (iz0 & hashMask)];
        // int h001 = hash [h00 + (iz1 & hashMask)];
        // int h101 = hash [h10 + (iz1 & hashMask)];
        // int h011 = hash [h01 + (iz1 & hashMask)];
        // int h111 = hash [h11 + (iz1 & hashMask)];

        // return Maths.lerp (
        //     Maths.lerp (Maths.lerp (h000, h100, tx), Maths.lerp (h010, h110, tx), ty),
        //     Maths.lerp (Maths.lerp (h001, h101, tx), Maths.lerp (h011, h111, tx), ty),
        //     tz
        // ) / hashMask; 
    }

    public static double fbm (double x, double y, double z, double frequency, int octaves, double gain, double lacunarity)
    {
        double sum = 0.0;
        double amplitude = 1.0;
        double range = 0.0;

        for (int o = 0; o < octaves; o++) {
            sum += value3D(x, y, z, frequency) * amplitude;
            range += amplitude;
            amplitude *= gain;
            frequency *= lacunarity;
        }
        return sum / range;
    }

    public static double [] domainWarpOffsets = {
        2.4, 17.2, 90.4, 85.2, 3.4, 67.2, 40.6, 20.4, 30.9
    };

    public static double fPattern (double x, double y, double z, double frequency, int octaves, double gain, double lacunarity)
    {
        double x0 = fbm (
                        x + domainWarpOffsets [0],
                        y + domainWarpOffsets [1],
                        z + domainWarpOffsets [2],
                        frequency, octaves, gain, lacunarity
                    );
        double y0 = fbm (
                        x + domainWarpOffsets [3],
                        y + domainWarpOffsets [4],
                        z + domainWarpOffsets [5],
                        frequency, octaves, gain, lacunarity
                    );
        double z0 = fbm (
                        x + domainWarpOffsets [6],
                        y + domainWarpOffsets [7],
                        z + domainWarpOffsets [8],
                        frequency, octaves, gain, lacunarity
                    );
        return fbm (x0, y0, z0, frequency, octaves, gain, lacunarity);
    }
}
