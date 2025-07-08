package com.ameen.chat_ai.response;

import com.ameen.chat_ai.constants.Constant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = Constant.SERIAL_VERSION_UID;

    private long totalRecordCount;
    private boolean hasNext;
    private boolean hasPrevious;
    private transient T data;

    public PageResponse(List<Object> objects, int i, boolean hasPrevious, boolean b) {
    }
}
