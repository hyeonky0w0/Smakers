package com.example.smakersbe.asset.repository;

import com.example.smakersbe.asset.entity.UserAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAssetRepository extends JpaRepository<UserAsset, Long> {

    Optional<UserAsset> findByUser_UserIdAndAsset_AssetId(Long userId, Long assetId);

    // 유저의 UserAsset을 asset까지 한 번에 가져오기 (N+1 방지)
    @Query("""
        select ua
        from UserAsset ua
        join fetch ua.asset a
        where ua.user.userId = :userId
    """)
    List<UserAsset> findAllByUserIdFetchAsset(@Param("userId") Long userId);

    @Query("""
    select ua
    from UserAsset ua
    join fetch ua.asset a
    where ua.user.uuid = :uuid
""")
    List<UserAsset> findAllByUserUuidFetchAsset(@Param("uuid") String uuid);





}