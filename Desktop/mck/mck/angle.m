function ret=angle(c,n1,n2,n3)
    
    %RB numbers need to be entered in order hip/shoulder, foot/hand,
    %knee/elbow
    
    %returns the supplement angle to the angle formed by the vectors from
    %the hip to the knee and the knee to the foot, so that the angle is 180
    %when standing up
    
    %angle function based on geometrical definitions of scalar and vector
    %products
    AngleBetweenVectors=@(u,v) atan2d(norm(cross(u,v)),dot(u,v));
    
    A=RBPosVector(n1,n2,n3,c);
    
    theta=180-AngleBetweenVectors(A(1:3,3)-A(1:3,1),A(1:3,2)-A(1:3,3));
    
    ret = theta;            
    
end