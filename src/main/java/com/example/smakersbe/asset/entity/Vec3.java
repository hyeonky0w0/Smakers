package com.example.smakersbe.asset.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vec3 {
    private double x;
    private double y;
    private double z;
}
