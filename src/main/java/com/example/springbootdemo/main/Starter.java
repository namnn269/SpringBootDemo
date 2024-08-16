package com.example.springbootdemo.main;

import com.example.springbootdemo.entity.MMaterial;
import com.example.springbootdemo.entity.MMaterialByVendor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.query.sqm.tree.select.SqmSortSpecification;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
public class Starter implements CommandLineRunner {

    private final EntityManager entityManager;

    @Override
    public void run(String... args) throws Exception {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dto> cq = builder.createQuery(Dto.class);
        Root<MMaterialByVendor> root = cq.from(MMaterialByVendor.class);

        Expression<Long> count = builder.count(root.get("materialCode"));
        Path<String> sysUserCodeRoot = root.get("systemUserCode");
        Path<String> whGroupCodeRoot = root.get("warehouseGroupCode");

        Join<MMaterialByVendor, MMaterial> materialCodeJoin = root.join("material", JoinType.INNER);
        Path<String> sysUserCodeMaterial = materialCodeJoin.get("systemUserCode");
        Path<String> whGroupCodeMaterial = materialCodeJoin.get("warehouseGroupCode");
        Path<String> fDeleteMaterial = materialCodeJoin.get("fDelete");

        Expression<String> whGCodeCoalesce = builder.coalesce(whGroupCodeMaterial, "s");

        Subquery<String> subQueryWhere = cq.subquery(String.class);
        Root<MMaterial> rootSubWhere = subQueryWhere.from(MMaterial.class);
        Path<Boolean> systemUserCodeSubWhere = rootSubWhere.get("systemUserCode");
        Path<String> whGroupCodeSubWhere = rootSubWhere.get("warehouseGroupCode");
        subQueryWhere.where(builder.equal(systemUserCodeSubWhere, "LOGICOM"))
                .select(whGroupCodeSubWhere)
                .distinct(true);

        Subquery<String> subQuerySelect = cq.subquery(String.class);
        Root<MMaterial> rootSubSelect = subQuerySelect.from(MMaterial.class);
        Path<Boolean> systemUserCodeSubSelect = rootSubSelect.get("systemUserCode");
        Path<String> whGroupCodeSubSelect = rootSubSelect.get("warehouseGroupCode");
        Path<Integer> materialSizeDSelect = rootSubSelect.get("materialSizeD");
        subQuerySelect.where(builder.equal(systemUserCodeSubSelect, "LOGICOM"))
//                .groupBy(whGroupCodeSubSelect)
                .select(builder.concat(whGroupCodeSubSelect ,"=="));

        materialCodeJoin
                .on(builder.equal(sysUserCodeMaterial, "LOGICOM"))
                .on(builder.equal(whGroupCodeMaterial, whGroupCodeRoot))
                .on(builder.equal(fDeleteMaterial, 0))
                .on(builder.or(builder.isNotNull(whGroupCodeMaterial), whGroupCodeMaterial.in(subQueryWhere)))
                .on(builder.concat(whGCodeCoalesce, "alo").in("10alo", "30alo", "po"));

        cq
                .where(builder.equal(sysUserCodeRoot, "LOGICOM"),
                        builder.like(sysUserCodeMaterial, "%COM%"))
                .groupBy(sysUserCodeRoot, whGroupCodeRoot)
                .having(builder.greaterThan(count, 1L))
                .multiselect(sysUserCodeRoot, whGroupCodeRoot, count, subQuerySelect)
                .orderBy(builder.asc(whGroupCodeRoot), builder.desc(count));

        TypedQuery<Dto> query = entityManager.createQuery(cq);
        List<Dto> resultList = query.getResultList();
        System.out.println(resultList.size());
        System.out.println(resultList);

    }

    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class Dto {
        private String systemUserCode;
        private String warehouseGroupCode;
        private long count;
        private String lol;
    }
}
