function R=RBPosVector(a, natnetClient)
    
    %returns a vector from the origin to the desired RB
    R=[natnetClient.getFrame('rigidbody',a).RigidBody.x; natnetClient.getFrame('rigidbody',a).RigidBody.y; natnetClient.getFrame('rigidbody',a).RigidBody.z];
    
end
