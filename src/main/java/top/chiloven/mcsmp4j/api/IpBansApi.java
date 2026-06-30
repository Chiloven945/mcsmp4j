package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.IncomingIpBan;
import top.chiloven.mcsmp4j.model.IpBan;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Type-safe client for the {@code minecraft:ip_bans} method group.
 *
 * <p>IP bans block connections from network addresses rather than player identities. The protocol distinguishes
 * between
 * {@link top.chiloven.mcsmp4j.model.IncomingIpBan} values used when adding bans and
 * {@link top.chiloven.mcsmp4j.model.IpBan} values returned by the server. This mirrors Minecraft's ability to store ban
 * metadata such as reason, source, creation time, and expiration.</p>
 *
 * <h2>Protocol mapping</h2>
 *
 * <ul>
 *     <li>{@link #list()} maps to {@code minecraft:ip_bans}</li>
 *     <li>{@link #set(java.util.Collection)} maps to {@code minecraft:ip_bans/set}</li>
 *     <li>{@link #add(java.util.Collection)} maps to {@code minecraft:ip_bans/add}</li>
 *     <li>{@link #remove(java.util.Collection)} maps to {@code minecraft:ip_bans/remove}</li>
 *     <li>{@link #clear()} maps to {@code minecraft:ip_bans/clear}</li>
 * </ul>
 *
 * <p>Use this API carefully in public tools. IP addresses are personal data in many jurisdictions, and ban lists should be
 * logged, exported, or displayed only when appropriate for the server operator's policy.</p>
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
