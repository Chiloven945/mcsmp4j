package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.IncomingIpBan;
import top.chiloven.mcsmp4j.model.IpBan;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strongly typed API for {@code minecraft:ip_bans}.
 */
public interface IpBansApi {

    CompletableFuture<List<IpBan>> list();

    CompletableFuture<List<IpBan>> clear();

    default CompletableFuture<List<IpBan>> set(IpBan... bans) {
        return set(List.of(bans));
    }

    CompletableFuture<List<IpBan>> set(Collection<IpBan> bans);

    default CompletableFuture<List<IpBan>> add(IncomingIpBan... bans) {
        return add(List.of(bans));
    }

    CompletableFuture<List<IpBan>> add(Collection<IncomingIpBan> bans);

    default CompletableFuture<List<IpBan>> remove(String... ipAddresses) {
        return remove(List.of(ipAddresses));
    }

    CompletableFuture<List<IpBan>> remove(Collection<String> ipAddresses);

}
