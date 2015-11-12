package none.lwjgl.components.assets;

import none.engine.component.assets.Assets;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple handler.
 */
class BaseHandler<T, K> {
    private final Map<K, T> loadedResources;
    private final Map<T, Integer> invokeCounter;
    private final Assets assets;

    protected BaseHandler(Assets assets) {
        this.loadedResources = new HashMap<>();
        invokeCounter = new HashMap<>();
        this.assets = Objects.requireNonNull(assets);
    }

    protected Assets getAssets() {
        return assets;
    }

    /**
     * Checks if a resource with same path have already been loaded.
     *
     * @param path Path to resource.
     * @return The resource in case it was already loaded.
     */
    protected T checkLoaded(K path) {
        T resource = loadedResources.getOrDefault(path, null);
        if (resource == null) {
            return null;
        } else {
            invokeCounter.put(resource, invokeCounter.get(resource) + 1);
            return resource;
        }
    }

    /**
     * Adds a new Resource to internal map.
     *
     * @param path     Path to resource.
     * @param resource The loaded resource.
     */
    protected void addResource(K path, T resource) {
        Objects.requireNonNull(resource, "resource");
        Objects.requireNonNull(path, "path");

        loadedResources.put(path, resource);
        invokeCounter.put(resource, 1);
    }

    /**
     * Checks if resource has to be disposed.
     *
     * @param resource The loaded resource.
     * @param path     Path to resource.
     * @return true, when disposal is necessary.
     */
    protected boolean dispose(T resource, K path) {
        if (loadedResources.getOrDefault(path, null) == null) {
            throw new IllegalStateException("Desired resource for disposal is not loaded.");
        }

        invokeCounter.put(resource, invokeCounter.get(resource) - 1);
        if (invokeCounter.get(resource) == 0) {
            loadedResources.remove(path);
            invokeCounter.remove(resource);

            return true;
        }

        return false;
    }
}
