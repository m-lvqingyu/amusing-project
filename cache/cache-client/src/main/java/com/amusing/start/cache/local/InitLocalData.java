package com.amusing.start.cache.local;

/**
 * 本地缓存数据初始化和重置
 * Create By 2021/8/7
 *
 * @author lvqingyu
 */
public interface InitLocalData<K, V> {

    /**
     * 根据key初始化数据
     *
     * @param k
     * @return
     */
    V initData(K k);

    /**
     * 根据key重置数据
     *
     * @param k
     * @return
     */
    V resetInitData(K k);
}
