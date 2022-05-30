package com.iotinall.canteen.dto.dish;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜品类别树
 *
 * @author loki
 * @date 2020/03/28 11:56
 */
@Data
@Accessors(chain = true)
public class CuisineTree implements Serializable {
    private String id;
    private String pid;
    private String label;
    private List<CuisineTree> children;

    public Object listToTree(List<SysMasterCuisineDTO> cuisineList) {
        if (CollectionUtils.isEmpty(cuisineList)) {
            return null;
        }

        List<CuisineTree> trees = new ArrayList<>();
        CuisineTree tree;
        for (SysMasterCuisineDTO mc : cuisineList) {
            if (null == mc) {
                continue;
            }

            tree = new CuisineTree();
            tree.setLabel(mc.getName());
            tree.setId(mc.getId());

            if (!CollectionUtils.isEmpty(mc.getCuisines())) {
                List<CuisineTree> childs = new ArrayList<>(mc.getCuisines().size());
                CuisineTree child;
                for (SysCuisineDTO c : mc.getCuisines()) {
                    child = new CuisineTree();
                    child.setLabel(c.getName());
                    child.setId(c.getId());
                    child.setPid(mc.getId());
                    childs.add(child);
                }
                tree.setChildren(childs);
            }

            trees.add(tree);
        }
        return trees;
    }
}
