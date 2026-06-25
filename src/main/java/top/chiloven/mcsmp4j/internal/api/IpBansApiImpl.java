package top.chiloven.mcsmp4j.internal.api;

import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.api.IpBansApi;
import top.chiloven.mcsmp4j.model.IncomingIpBan;
import top.chiloven.mcsmp4j.model.IpBan;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public final class IpBansApiImpl implements IpBansApi {

    private static final String BASE = "minecraft:ip_bans";

    private final RawApi raw;

    public IpBansApiImpl(RawApi raw) {
        this.raw = requireNonNull(raw, "raw");
    }

    @Override
    public CompletableFuture<List<IpBan>> list() {
        return ApiSupport.ipBanList(raw, BASE);
    }

    @Override
    public CompletableFuture<List<IpBan>> clear() {
        return ApiSupport.ipBanList(raw, BASE + "/clear");
    }

    @Override
    public CompletableFuture<List<IpBan>> set(Collection<IpBan> bans) {
        return ApiSupport.ipBanList(raw, BASE + "/set", ApiSupport.copy(bans, "bans"));
    }

    @Override
    public CompletableFuture<List<IpBan>> add(Collection<IncomingIpBan> bans) {
        return ApiSupport.ipBanList(raw, BASE + "/add", ApiSupport.copy(bans, "bans"));
    }

    @Override
    public CompletableFuture<List<IpBan>> remove(Collection<String> ipAddresses) {
        return ApiSupport.ipBanList(raw, BASE + "/remove", ApiSupport.copyStrings(ipAddresses, "ipAddresses"));
    }

}
