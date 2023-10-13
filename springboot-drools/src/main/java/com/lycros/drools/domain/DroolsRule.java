package com.lycros.drools.domain;
 
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
 
/**
 * drools 规则实体类
 *
 */
@Data
@Entity
@Table(name = "drools_rule")
public class DroolsRule {
 
    /**
     * 规则id
     */
    @Id
    @GeneratedValue
    private Long ruleId;


    /**
     * kbase的名字
     */
    private String kieBaseName;
    /**
     * 设置该kbase需要从那个目录下加载文件，这个是一个虚拟的目录，相对于 `src/main/resources`
     * 比如：kiePackageName=rules/rule01 那么当前规则文件写入路径为： kieFileSystem.write("src/main/resources/rules/rule01/1.drl")
     */
    private String kiePackageName;
    /**
     * 规则内容
     */
    private String ruleContent;
    /**
     * 规则创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    /**
     * 规则更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
 