package com.example.springbootdemo.main;

import com.example.springbootdemo.infrastructure.database.logicom.entity.MMaterial;
import com.example.springbootdemo.infrastructure.database.logicom.entity.MMaterialByVendor;
import com.example.springbootdemo.infrastructure.database.logicom.entity.MMaterialByVendor_;
import com.example.springbootdemo.infrastructure.database.logicom.entity.MMaterial_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

//@Component
@AllArgsConstructor
public class CriteriaJpaStarter implements CommandLineRunner {

    private final EntityManager entityManager;

    @Override
    public void run(String... args) throws Exception {
        String system = "LOGICOM";

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> tupleCq = builder.createQuery(Tuple.class);
        CriteriaQuery<Dto> dtoCq = builder.createQuery(Dto.class);

        buildCriteriaQuery(builder, tupleCq, system);
        buildCriteriaQuery(builder, dtoCq, system);

        TypedQuery<Dto> dtoQuery = entityManager.createQuery(dtoCq);
        List<Dto> dtoResult = dtoQuery.getResultList();
        System.out.println(dtoResult);

        TypedQuery<Tuple> tupleQuery = entityManager.createQuery(tupleCq);
        List<Tuple> tupleList = tupleQuery.getResultList();
        for (Tuple tuple : tupleList) {
            String s = String.join("=>",
                    tuple.get("sys", String.class),
                    tuple.get("whg", String.class),
                    String.valueOf(tuple.get("count", Long.class)),
                    String.valueOf(tuple.get("size", Integer.class))
            );
            System.out.println(s);
        }

    }

    private <T> void buildCriteriaQuery(CriteriaBuilder builder, CriteriaQuery<T> cq, String system) {
        Root<MMaterialByVendor> root = cq.from(MMaterialByVendor.class);
        Expression<Long> count = builder.count(root.get(MMaterialByVendor_.materialCode));
        Path<String> sysUserCodeRoot = root.get(MMaterialByVendor_.systemUserCode);
        Path<String> whGroupCodeRoot = root.get(MMaterialByVendor_.warehouseGroupCode);

        // join material
        Join<MMaterialByVendor, MMaterial> materialCodeJoin = root.join(MMaterialByVendor_.material, JoinType.INNER);
        Path<String> sysUserCodeMaterial = materialCodeJoin.get(MMaterial_.systemUserCode);
        Path<String> whGroupCodeMaterial = materialCodeJoin.get(MMaterial_.warehouseGroupCode);
        Path<Integer> fDeleteMaterial = materialCodeJoin.get(MMaterial_.fDelete);

        Expression<String> whGCodeCoalesce = builder.coalesce(whGroupCodeMaterial, "s");

        // subQuery for select
        Subquery<String> subQueryWhere = cq.subquery(String.class);
        Root<MMaterial> rootSubWhere = subQueryWhere.from(MMaterial.class);
        Path<String> systemUserCodeSubWhere = rootSubWhere.get(MMaterial_.systemUserCode);
        Path<String> whGroupCodeSubWhere = rootSubWhere.get(MMaterial_.warehouseGroupCode);
        subQueryWhere.where(builder.equal(systemUserCodeSubWhere, system))
                .select(whGroupCodeSubWhere)
                .distinct(true);

        Subquery<Integer> subQuerySelect = cq.subquery(Integer.class);
        Root<MMaterial> rootSubSelect = subQuerySelect.from(MMaterial.class);
        Path<String> systemUserCodeSubSelect = rootSubSelect.get(MMaterial_.systemUserCode);
        Path<Integer> materialSizeDSelect = rootSubSelect.get(MMaterial_.materialSizeD);

        subQuerySelect.where(builder.equal(systemUserCodeSubSelect, system))
                .select(builder.max(materialSizeDSelect));

        materialCodeJoin
                .on(builder.equal(sysUserCodeMaterial, system))
                .on(builder.equal(whGroupCodeMaterial, whGroupCodeRoot))
                .on(builder.equal(fDeleteMaterial, 0))
                .on(builder.or(builder.isNotNull(whGroupCodeMaterial), whGroupCodeMaterial.in(subQueryWhere)))
                .on(builder.concat(whGCodeCoalesce, "alo").in("10alo", "30alo", "po"));

        cq
                .where(builder.equal(sysUserCodeRoot, system),
                        builder.like(sysUserCodeMaterial, "%COM%"))
                .groupBy(sysUserCodeRoot, whGroupCodeRoot)
                .having(builder.greaterThan(count, 1L))
                .multiselect(
                        sysUserCodeRoot.alias("sys"),
                        whGroupCodeRoot.alias("whg"),
                        count.alias("count"),
                        subQuerySelect.alias("size"))
                .orderBy(builder.asc(whGroupCodeRoot), builder.desc(count));
    }

    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class Dto {
        private String systemUserCode;
        private String warehouseGroupCode;
        private long count;
        private Integer lol;
    }
}
