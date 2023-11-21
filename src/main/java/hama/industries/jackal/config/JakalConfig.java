package hama.industries.jackal.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class JakalConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.IntValue PRIMARY_CL_CONTROL_RADIUS;
    
    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        PRIMARY_CL_CONTROL_RADIUS = builder.defineInRange("primary_cl_control_radius", 7, 0, 30);
    }
}
