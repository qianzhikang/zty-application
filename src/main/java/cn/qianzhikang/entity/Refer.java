package cn.qianzhikang.entity;

import lombok.Data;

import java.util.List;

/**
 * @author qianzhikang
 */
@Data
public class Refer {
    private List<String> sources;
    private List<String> license;
}
