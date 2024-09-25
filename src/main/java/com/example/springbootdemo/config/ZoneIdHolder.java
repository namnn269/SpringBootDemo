package com.example.springbootdemo.config;

import java.time.ZoneId;
import java.util.function.Supplier;

public class ZoneIdHolder {

    private ZoneIdHolder() {
    }

    private static final ThreadLocal<Supplier<ZoneId>> ZONE_ID_HOLDER = new ThreadLocal<>();

    public static void setZoneId(ZoneId zoneId) {
        ZONE_ID_HOLDER.set(() -> zoneId);
    }

    public static void setDeferredZoneId(Supplier<ZoneId> zoneIdSupplier) {
        ZONE_ID_HOLDER.set(zoneIdSupplier);
    }

    public static ZoneId getZoneId() {
        return getDeferredZoneId().get();
    }

    public static Supplier<ZoneId> getDeferredZoneId() {
        Supplier<ZoneId> zoneIdSupplier = ZONE_ID_HOLDER.get();
        if (zoneIdSupplier == null) {
            zoneIdSupplier = ZoneId::systemDefault;
            ZONE_ID_HOLDER.set(zoneIdSupplier);
        }
        return zoneIdSupplier;
    }

    public static void clear() {
        ZONE_ID_HOLDER.remove();
    }

}
