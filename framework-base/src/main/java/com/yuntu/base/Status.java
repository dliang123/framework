package com.yuntu.base;

import java.io.Serializable;

public interface Status extends Describable ,Serializable{

    Status end();
    Status cancel();

    default boolean isEnd()     { return this == end(); };
    default boolean isCancel()  { return this == cancel(); };
}
