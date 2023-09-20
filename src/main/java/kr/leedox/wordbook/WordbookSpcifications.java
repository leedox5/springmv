package kr.leedox.wordbook;

import kr.leedox.entity.Member;
import kr.leedox.entity.Wordbook;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class WordbookSpcifications {
    public static Specification<Wordbook> equalToAuthor(Member author) {
        return new Specification<Wordbook>() {
            @Override
            public Predicate toPredicate(Root<Wordbook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("author"), author);
            }
        };
    }

    public static Specification<Wordbook> likeWord(String key) {
        return new Specification<Wordbook>() {
            @Override
            public Predicate toPredicate(Root<Wordbook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("word"), key + "%");
            }
        };
    }

    public static Specification<Wordbook> likeMeaning1(String key) {
        return new Specification<Wordbook>() {
            @Override
            public Predicate toPredicate(Root<Wordbook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("meaning1"), "%" + key + "%");
            }
        };
    }

    public static Specification<Wordbook> greaterThanSeq(String key) {
        return new Specification<Wordbook>() {
            @Override
            public Predicate toPredicate(Root<Wordbook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("seq"), Integer.parseInt(key));
            }
        };
    }

    public static Specification<Wordbook> equalToSeq(Integer seq) {
        return new Specification<Wordbook>() {
            @Override
            public Predicate toPredicate(Root<Wordbook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("seq"), seq);
            }
        };
    }

    public static Specification<Wordbook> likeMeaning2(String key) {
        return new Specification<Wordbook>() {
            @Override
            public Predicate toPredicate(Root<Wordbook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("meaning2"), key + "%");
            }
        };
    }

    public static Specification<Wordbook> equalToMeaning2(String key) {
        return new Specification<Wordbook>() {
            @Override
            public Predicate toPredicate(Root<Wordbook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("meaning2"), key);
            }
        };
    }
}
