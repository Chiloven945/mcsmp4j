package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.IncomingIpBan;
import top.chiloven.mcsmp4j.model.IpBan;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strongly typed client for the {@code minecraft:ip_bans} method group.
 *
 * <p>IP bans reject connections from specific remote IP addresses. Add operations use {@link IncomingIpBan}
 * because the protocol allows the request to identify the ban either by raw address or by player information. Returned
 * entries use {@link IpBan} because the server resolves them into concrete ban-list entries.</p>
 */
public interface IpBansApi {

    /**
     * Gets the current IP ban list.
     *
     * @return a future containing all IP ban entries known to the server
     */
    CompletableFuture<List<IpBan>> list();

    /**
     * Clears the entire IP ban list.
     *
     * @return a future containing the IP ban list after clearing, usually an empty list
     */
    CompletableFuture<List<IpBan>> clear();

    /**
     * Replaces the IP ban list with the supplied entries.
     *
     * @param bans the entries that should become the complete IP ban list
     *
     * @return a future containing the IP ban list after replacement
     */
    default CompletableFuture<List<IpBan>> set(IpBan... bans) {
        return set(List.of(bans));
    }

    /**
     * Replaces the IP ban list with the supplied entries.
     *
     * @param bans the entries that should become the complete IP ban list
     *
     * @return a future containing the IP ban list after replacement
     */
    CompletableFuture<List<IpBan>> set(Collection<IpBan> bans);

    /**
     * Adds IP ban requests.
     *
     * @param bans the IP ban requests to add
     *
     * @return a future containing the resolved IP ban list after the addition
     */
    default CompletableFuture<List<IpBan>> add(IncomingIpBan... bans) {
        return add(List.of(bans));
    }

    /**
     * Adds IP ban requests.
     *
     * @param bans the IP ban requests to add
     *
     * @return a future containing the resolved IP ban list after the addition
     */
    CompletableFuture<List<IpBan>> add(Collection<IncomingIpBan> bans);

    /**
     * Removes IP bans by raw IP address.
     *
     * @param ipAddresses the IP addresses to remove from the ban list
     *
     * @return a future containing the IP ban list after removal
     */
    default CompletableFuture<List<IpBan>> remove(String... ipAddresses) {
        return remove(List.of(ipAddresses));
    }

    /**
     * Removes IP bans by raw IP address.
     *
     * @param ipAddresses the IP addresses to remove from the ban list
     *
     * @return a future containing the IP ban list after removal
     */
    CompletableFuture<List<IpBan>> remove(Collection<String> ipAddresses);

}
