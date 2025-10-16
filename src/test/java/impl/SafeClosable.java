package impl;

/**
 * @author ZZZank
 */
public interface SafeClosable extends AutoCloseable {
    @Override
    void close();
}
